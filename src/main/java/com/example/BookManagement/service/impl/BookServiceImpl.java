package com.example.BookManagement.service.impl;

import com.example.BookManagement.configuration.properties.AppCacheProperties;
import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
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
        if (bookRepository.findByAuthor(author) == null) {
            throw new EntityNotFoundException(MessageFormat.format("Book with author {0} not found", author));
        }
        List<Book> books = bookRepository.findByAuthor(author);
        Book ourBook = null;
        for (Book book : books) {
            if (book.getTitle().equals(title))
                ourBook = book;
        }

        if (ourBook == null) {
            throw new EntityNotFoundException(MessageFormat.format("Book with author with  title {0} not found", title));
        } else
            log.info("если распечаталось, то объекты взяты из БД postgresql, а если не распечаталось, то объекты  взяты из кэш");
        return ourBook;
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITY_BY_CATEGORY, key = "#category")
    @Override
    public List<Book> findCategory(String category) {
        if (categoryRepository.findByCategory(category).getBooks() == null) {
            throw new EntityNotFoundException(MessageFormat.format("Books with category {0} not found", category));
        }
        List<Book> ourBook = bookRepository.findByCategory_id(categoryRepository.findByCategory(category).getId());
        log.info("если распечаталось, то объекты взяты из БД postgresql, а если не распечаталось, то объекты  взяты из кэш");
        return ourBook;
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> userOptional = bookRepository.findById(id);
        if (userOptional.isPresent()) {
            return bookRepository.findById(id).get();
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Book with ID {0} not found", id));
        }
    }

    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    @Override
    public ResponseEntity<String> save(Book book, Category category) {
        this.noCategory(category);
        Category category1 = categoryRepository.findByCategory(category.getCategory());
        book.setCategory(category1);
        bookRepository.save(book);
        return ResponseEntity.ok(MessageFormat.format("Book with title  {0} save", book.getTitle()));
    }

    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    @Override
    public ResponseEntity<String> update(Book book, Category category) {
        this.noCategory(category);
        Category category1 = categoryRepository.findByCategory(category.getCategory());
        book.setCategory(category1);
        Book existedBook = this.findById(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category1);
        bookRepository.save(existedBook);
        return ResponseEntity.ok(MessageFormat.format("Book with ID {0} updated", book.getId()));
    }


    @Caching(evict = {@CacheEvict(value = "databaseEntity", allEntries = true),
            @CacheEvict(value = "databaseEntitiesByCategory", allEntries = true)})
    public ResponseEntity<String> deleteById(Long id) {
        Optional<Book> userOptional = bookRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(MessageFormat.format("Book with ID {0} not found", id));
        } else {
            bookRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("Book with ID {0}  deleted", id));
        }
    }

    public void noCategory(Category category) {
        if (categoryRepository.findAll().stream().noneMatch(category1 -> category1.getCategory().equals(category.getCategory()))) {
            categoryRepository.save(category);
        }
    }
}
