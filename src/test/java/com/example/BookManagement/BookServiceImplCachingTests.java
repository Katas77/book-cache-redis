package com.example.BookManagement;


import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CategoryRepository;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookServiceImplCachingTests.TestConfig.class, BookServiceImpl.class})
public class BookServiceImplCachingTests {
    @Autowired
    private BookInterface bookService;

    @Configuration
    @EnableCaching // включаем кэширование в тестовом контексте
    static class TestConfig {
        @Bean
        public BookRepository bookRepository() {
            return Mockito.mock(BookRepository.class);
        }

        @Bean
        public CategoryRepository categoryRepository() {
            return Mockito.mock(CategoryRepository.class);
        }

        @Bean
        public ConcurrentMapCacheManager cacheManager() {
            // in-memory замена Redis для unit-тестов
            return new ConcurrentMapCacheManager(
                    com.example.BookManagement.configuration.properties.AppCacheProperties.CacheNames.DATABASE_ENTITY,
                    com.example.BookManagement.configuration.properties.AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY
            );
        }
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;



    private Book sample;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sample = new Book();
        sample.setId(1L);
        sample.setTitle("Test Title");
        sample.setAuthor("Test Author");

        sampleCategory = new Category();
        sampleCategory.setId(10L);
        sampleCategory.setCategory("SCIENCE");

        Mockito.reset(bookRepository, categoryRepository);
    }

    @Test
    void whenFindByIdCalledTwice_repositoryCalledOnce() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sample));

        Book first = bookService.findById(1L);
        Book second = bookService.findById(1L);

        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        assertThat(first).isSameAs(second); // в ConcurrentMapCache возвращается тот же объект

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void whenFindByTitleCalledTwice_repositoryCalledOnce() {
        when(bookRepository.findByAuthorAndTitle("Test Author", "Test Title")).thenReturn(Optional.of(sample));

        Book first = bookService.findByTitle("Test Title", "Test Author");
        Book second = bookService.findByTitle("Test Title", "Test Author");

        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        assertThat(first).isSameAs(second);

        verify(bookRepository, times(1)).findByAuthorAndTitle("Test Author", "Test Title");
    }

    @Test
    void whenSave_thenCachePut_and_findByIdDoesNotCallRepository() {
        Book toSave = new Book();
        toSave.setTitle("New Book");
        toSave.setAuthor("New Author");

        Book saved = new Book();
        saved.setId(42L);
        saved.setTitle("New Book");
        saved.setAuthor("New Author");
        saved.setCategory(sampleCategory);

        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        // save должен вернуть Book и @CachePut положить его в кэш по #result.id
        Book result = bookService.save(toSave, sampleCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(42L);

        clearInvocations(bookRepository); // очищаем счётчики вызовов для проверки кэша

        // при чтении по id repository.findById НЕ должен вызываться (значение из кэша)
        Book cached = bookService.findById(42L);
        assertThat(cached).isNotNull();
        assertThat(cached.getId()).isEqualTo(42L);

        verify(bookRepository, never()).findById(42L);
    }
}
