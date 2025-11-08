package com.example.BookManagement.service.impl;

import com.example.BookManagement.exception.BusinessLogicException;
import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static com.example.BookManagement.configuration.redis.condition.AppConstants.CACHE_DATABASE_ENTITIES_BY_CATEGORY;
import static com.example.BookManagement.configuration.redis.condition.AppConstants.CACHE_DATABASE_ENTITY;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookInterface {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Кэш: DATABASE_ENTITY, ключ = title::author
     */
    @Override
    @Cacheable(cacheNames = CACHE_DATABASE_ENTITY, key = "#p0 + '::' + #p1")
    public Book findByTitle(String title, String author) {
        return bookRepository.findByAuthorAndTitle(author, title)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Book with title {0} and author {1} not found", title, author)));
    }

    /**
     * Кэш: DATABASE_ENTITIES_BY_CATEGORY, ключ = category (имя категории)
     */
    @Override
    @Cacheable(cacheNames = CACHE_DATABASE_ENTITIES_BY_CATEGORY, key = "#p0")
    public List<Book> findCategory(String categoryName) {
        Category category = categoryRepository.findByCategory(categoryName)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Category {0} not found", categoryName)));

        List<Book> books = bookRepository.findByCategoryId(category.getId());
        log.info("Книги по категории '{}' найдены: {}", categoryName, books.size());
        return books;
    }

    /**
     * Кэш: DATABASE_ENTITY, ключ = id, не кешировать null-результат
     */
    @Override
    @Cacheable(cacheNames = CACHE_DATABASE_ENTITY, key = "#p0", unless = "#result == null")
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Book with ID {0} not found", id)));
    }

    /**
     * После сохранения помещаем объект в CACHE_DATABASE_ENTITY по #result.id
     * и очищаем все записи по категориям
     */
    @Override
    @Transactional
    @Caching(
            put = @CachePut(cacheNames = CACHE_DATABASE_ENTITY, key = "#result.id"),
            evict = @CacheEvict(cacheNames = CACHE_DATABASE_ENTITIES_BY_CATEGORY, allEntries = true)
    )
    public Book save(Book book) {
        Book saved = bookRepository.save(book);
        log.info("Книга сохранена с ID {}", saved.getId());
        return saved;
    }

    /**
     * После обновления — удалить по id из DATABASE_ENTITY и очистить списки категорий
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_DATABASE_ENTITY, key = "#p0"),
            @CacheEvict(cacheNames = CACHE_DATABASE_ENTITIES_BY_CATEGORY, allEntries = true)
    })
    public String update(Long id, Book book) {
        Book existedBook = findById(id);
        BeanUtils.copyNonNullProperties(book, existedBook);
        Book saved = bookRepository.save(existedBook);
        log.info("Книга с ID {} обновлена", saved.getId());
        return MessageFormat.format("Книга с ID {0} обновлена", saved.getId());
    }

    /**
     * После удаления — удалить запись по id и очистить списки категорий
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_DATABASE_ENTITY, key = "#p0"),
            @CacheEvict(cacheNames = CACHE_DATABASE_ENTITIES_BY_CATEGORY, allEntries = true)
    })
    public String deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessLogicException(MessageFormat.format("Book with ID {0} not found", id));
        }
        bookRepository.deleteById(id);
        log.info("Книга с ID {} удалена", id);
        return MessageFormat.format("Книга с ID {0} удалена", id);
    }
}
