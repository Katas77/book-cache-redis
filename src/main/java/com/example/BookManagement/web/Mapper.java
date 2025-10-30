package com.example.BookManagement.web;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import com.example.BookManagement.web.dto.BookRequest;
import com.example.BookManagement.web.dto.BookResponse;



@Component
@RequiredArgsConstructor
public class Mapper {
    private final CategoryRepository repository;
    private final BookRepository bookRepository;
    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory() != null ? book.getCategory().getCategory() : null
        );
    }

    public List<BookResponse> toBookResponseList(List<Book> books) {
        return books.stream()
                .map(this::toResponse)
                .toList();
    }


    public Book requestToBook(BookRequest request) {
        request.validate();
        Category category = null;
        if (request.category() != null) {
            Optional<Category> existing = repository.findByCategory(request.category());
            if (existing.isPresent()) {
                category = existing.get();
            } else {
                category = new Category();
                category.setCategory(request.category());

                category = repository.save(category);
            }
        }
        return Book
                .builder()
                .title(request.title())
                .author(request.author())
                .category(category)
                .build();
    }


}
