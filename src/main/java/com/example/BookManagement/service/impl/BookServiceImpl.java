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
import org.springframework.cache.annotation.CachePut;
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

    /**
     * Поиск книги по title + author.
     *
     * @Cacheable: при вызове сначала пытается получить значение из кэша с именем
     * AppCacheProperties.CacheNames.DATABASE_ENTITY и ключом, вычисленным из SpEL.
     * - Если значение есть в кэше — метод НЕ выполняется, значение возвращается из кэша.
     * - Если значения нет — метод выполняется, результат помещается в кэш под этим ключом.
     * <p>
     * Здесь ключ составной: "#title + '::' + #author" — чтобы избежать коллизий
     * при одинаковых title у разных авторов. Обратите внимание:
     * - Использование одного и того же cacheName для разных способов поиска (по id и по title)
     * допустимо, но требует внимательной генерации ключей во избежание пересечений.
     */
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#title + '::' + #author")
    @Override
    public Book findByTitle(String title, String author) {
        System.out.println(": public Book findByTitle(String title, String author)     ");
        return bookRepository.findByAuthorAndTitle(author, title)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Book with title {0} and author {1} not found", title, author)));
    }

    /**
     * Поиск книг по категории.
     *
     * @Cacheable: кэшируется список книг по ключу категории (#category) в кэше
     * AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY.
     * - Рекомендуется учитывать, что списки меняются при добавлении/удалении/обновлении
     * книг — поэтому обязательно инвалидировать (evict) этот кэш при сохранении/удалении/обновлении.
     */
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY, key = "#category")
    @Override
    public List<Book> findCategory(String category) {
        long categoryId = categoryRepository.findByCategory(category)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Category {0} not found", category))).getId();
        List<Book> books = bookRepository.findByCategoryId(categoryId).stream().map(Optional::orElseThrow).toList();
        log.info("Книги по категории {} найдены: {}", category, books.size());
        return books;
    }

    /**
     * Поиск по id — кешируется в том же кеше DATABASE_ENTITY, ключ = id.
     * unless = "#result == null" — предотвращает кеширование null-результатов (хотя
     * в текущей реализации при отсутствии книги выбрасывается исключение и null не возвращается).
     */
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#id", unless = "#result == null")
    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(
                        MessageFormat.format("Book with ID {0} not found", id)));
    }

    /**
     * СОХРАНЕНИЕ книги.
     * <p>
     * Используем @CachePut чтобы после сохранения поместить в кэш актуальный объект Book.
     * ВАЖНО: @CachePut кэширует возврат метода (return value). Поэтому метод должен
     * возвращать сам объект Book (а не строку), если вы хотите кэшировать сущность.
     * <p>
     * Также важно инвалидировать (evict) кэш по спискам категорий — после добавления
     * книги списки для соответствующей категории устарели. Здесь для простоты инвалидируем
     * все записи ENTITY_BY_CATEGORY; при желании можно инвалидировать только конкретную категорию.
     */
    @Transactional
    @Caching(
            put = {@CachePut(value = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#result.id")},
            evict = {@CacheEvict(value = AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY, allEntries = true)}
    )
    @Override
    public Book save(Book book, Category category) {
        handleCategory(category);
        book.setCategory(category);
        Book saved = bookRepository.save(book);
        log.info("Книга сохранена с ID {}", saved.getId());
        return saved; // именно этот объект будет помещён в кэш (по ключу #result.id)
    }

    /**
     * Обновление книги.
     * <p>
     * Здесь используем @Caching с evict — после обновления удаляем из кэша запись по ID
     * (чтобы следующий запрос заново загрузил свежие данные) и очищаем кэш списков по категориям.
     * <p>
     * Обратите внимание на проблему self-invocation: внутри этого бина вызов findById(...) не
     * проходит через прокси и, следовательно, кэш для findById не будет использован при вызове
     * изнутри этого же класса. Если вам важно, чтобы findById использовал кэш и при внутреннем
     * вызове, рассмотрите разделение логики в два бина или включение AspectJ (exposeProxy).
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#book.id"),
            @CacheEvict(value = AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY, allEntries = true)
    })
    public String update(Book book, Category category) {
        handleCategory(category);

        Book existedBook = findById(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category);
        bookRepository.save(existedBook);
        return MessageFormat.format("Книга с ID {0} обновлена", book.getId());
    }

    /**
     * Удаление по id.
     * <p>
     * После удаления удаляем из кэша конкретную запись (ключ = id) и инвалидируем Инвалидация = удаление/очистка записи(ей) из кэша (evict/clear).
     * кэш списков по категориям (allEntries = true).
     * <p>
     * Если вы хотите гарантировать удаление кэша даже при выбрасывании исключения,
     * установите beforeInvocation = true в @CacheEvict (но тогда удаление произойдёт
     * до фактического удаления в БД).
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#id"),
            @CacheEvict(value = AppCacheProperties.CacheNames.ENTITY_BY_CATEGORY, allEntries = true)
    })
    @Override
    public String deleteById(Long id) {
        bookRepository.deleteById(id);
        return MessageFormat.format("Книга с ID {0} удалена", id);

    }

    private void handleCategory(Category category) {
        if (!categoryRepository.existsByCategory(category.getCategory())) {
            categoryRepository.save(category);
        }
    }
}
