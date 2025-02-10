package com.example.BookManagement.service.impl;

import com.example.BookManagement.configuration.properties.AppCacheProperties;
import com.example.BookManagement.exception.BusinessLogicException;
import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.utils.BeanUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookInterface {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES, key = "#title+#author")
    @Override
    public Book findByTitle(String title, String author) {
            return bookRepository.findByAuthorAndTitle(author, title)
                    .orElseThrow(() -> new BusinessLogicException(
                            MessageFormat.format("Book with title {0} and author {1} not found", title, author)));

    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITY_BY_CATEGORY, key = "#category")
    @Override
    public List<Book> findCategory(String category) {
        long categoryId = categoryRepository.findByCategory(category)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Category {0} not found", category))).getId();
        List<Book> books = bookRepository.findByCategoryId(categoryId).stream().map(Optional::orElseThrow).toList();
        log.info("Книги по категории {} найдены: {}", category, books.size());
        return books;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Book with ID {0} not found", id)));
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    @Override
    public String save(Book book, Category category) {
        noCategory(category);
        Category existingCategory = categoryRepository.findByCategory(category.getCategory())
                .orElseGet(() -> categoryRepository.save(category));
        book.setCategory(existingCategory);
        bookRepository.save(book);
        return MessageFormat.format("Книга с названием {0} сохранена", book.getId());
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    @Override
    public String update(Book book, Category category) {
        noCategory(category);
        Category existingCategory = categoryRepository.findByCategory(category.getCategory())
                .orElseGet(() -> categoryRepository.save(category));
        Book existedBook = findById(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(existingCategory);
        bookRepository.save(existedBook);
        return MessageFormat.format("Книга с ID {0} обновлена", book.getId());
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    @Override
    public String deleteById(Long id) {
        bookRepository.deleteById(id);
        return MessageFormat.format("Книга с ID {0} удалена", id);
    }

    private void noCategory(Category category) {
        if (!categoryRepository.existsByCategory(category.getCategory())) {
            categoryRepository.save(category);
        }
    }
}