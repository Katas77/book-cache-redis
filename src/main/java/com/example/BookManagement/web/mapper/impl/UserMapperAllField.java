package com.example.BookManagement.web.mapper.impl;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.web.dto.BookResponse;
import com.example.BookManagement.web.dto.BookRequest;
import com.example.BookManagement.web.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperAllField implements Mapper {
    @Override
    public Book requestToBook(Long id, BookRequest request) {
        if (request == null) {
            return null;
        }
        Book.BookBuilder book = Book.builder();
        book.id(id).title(request.getTitle()).author(request.getAuthor()).category(requestToCategory(request));
        return book.build();
    }

    @Override
    public Book requestToBook(BookRequest request) {
        if (request == null) {
            return null;
        }
        Book.BookBuilder book = Book.builder();
        book.title(request.getTitle()).author(request.getAuthor()).category(requestToCategory(request));
        return book.build();
    }

    @Override
    public Category requestToCategory(BookRequest request) {
        if (request.getCategory() == null) {
            return null;
        }
        Category.CategoryBuilder categoryBuilder = Category.builder();
        categoryBuilder.category(request.getCategory());
        return categoryBuilder.build();
    }

    @Override
    public BookResponse bookToResponse(Book book) {
        if (book == null) {
            return null;
        }
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setCategory(book.getCategory().getCategory());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setAuthor(book.getAuthor());
        return bookResponse;
    }
}


















