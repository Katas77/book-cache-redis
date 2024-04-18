

package com.example.BookManagement.listener;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.service.BookInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseTaskCreator {

    private final BookInterface bookInterface;


    @EventListener(ApplicationStartedEvent.class)
    public void createTaskData() {
        log.debug("Calling DatabaseTaskCreator->createTaskData...");
        Category category = Category.builder().category("Приключения").build();
        Category category2 = Category.builder().category("Природа").build();
        Book book = Book.builder().category(category).author("То́мас Майн Рид").title("В поисках белого бизона").build();
        Book book2 = Book.builder().category(category2).author("Виталий Бианки").title("Лесные сказки и были").build();
        Book book3 = Book.builder().category(category).author("Джон Гри́ффит Че́йни").title("Сердца трёх").build();
        bookInterface.save(book, category);
        bookInterface.save(book2, category2);
        bookInterface.save(book3, category);
    }


}

//DELETE FROM app_schema.category     DELETE FROM  app_schema.book