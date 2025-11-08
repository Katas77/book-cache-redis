package com.example.BookManagement.service.jackson_book;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(
        name = "app.book.dump.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class EditDump {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper; // инжектим настроенный ObjectMapper

    private static final Path BOOK_DUMP_PATH = Paths.get("data", "book.json");

    /**
     * Выполняется после старта приложения (когда прокси/транзакции уже готовы).
     * Обёрнут в транзакцию, чтобы корректно работать с репозиториями.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeDatabaseFromDumpIfEmpty() {
        if (bookRepository.count() == 0) {
            List<BookDto> dtos = loadBooksFromFile();
            if (!dtos.isEmpty()) {
                List<Book> books = toBookList(dtos);
                bookRepository.saveAll(books);
            }
        }
    }

    public void saveBooksToFile(List<Book> books) {
        List<BookDto> dtos = toDtoList(books);
        try {
            Files.createDirectories(BOOK_DUMP_PATH.getParent());
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(BOOK_DUMP_PATH.toFile(), dtos);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить книги в файл: " + BOOK_DUMP_PATH, e);
        }
    }

    public void clearBookFile() {
        try {
            Files.createDirectories(BOOK_DUMP_PATH.getParent());
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(BOOK_DUMP_PATH.toFile(), Collections.emptyList());
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось очистить файл книг: " + BOOK_DUMP_PATH, e);
        }
    }

    public List<BookDto> loadBooksFromFile() {
        if (!Files.exists(BOOK_DUMP_PATH)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(
                    BOOK_DUMP_PATH.toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить книги из файла: " + BOOK_DUMP_PATH, e);
        }
    }

    private List<BookDto> toDtoList(List<Book> books) {
        return books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getCategory() != null ? book.getCategory().getCategory() : null
                ))
                .toList();
    }

    private List<Book> toBookList(List<BookDto> dtos) {
        return dtos.stream()
                .map(dto -> {
                    Category category = null;
                    if (dto.categoryName() != null) {
                        Optional<Category> existing = categoryRepository.findByCategory(dto.categoryName());
                        if (existing.isPresent()) {
                            category = existing.get();
                        } else {
                            category = new Category();
                            category.setCategory(dto.categoryName());
                            category = categoryRepository.save(category);
                        }
                    }
                    return Book.builder()
                            .id(dto.id())
                            .title(dto.title())
                            .author(dto.author())
                            .category(category)
                            .build();
                })
                .toList();
    }
}
