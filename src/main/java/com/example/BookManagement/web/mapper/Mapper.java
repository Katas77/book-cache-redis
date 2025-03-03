package com.example.BookManagement.web.mapper;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.example.BookManagement.web.dto.BookListResponse;
import com.example.BookManagement.web.dto.BookResponse;
import com.example.BookManagement.web.dto.BookRequest;

import java.util.List;
import java.util.stream.Collectors;

public interface Mapper {
    Book requestToBook(Long id, BookRequest request);

    Book requestToBook(BookRequest request);

    Category requestToCategory(BookRequest request);

    BookResponse bookToResponse(Book book);

    default BookListResponse bookListResponseList(List<Book> books) {
        BookListResponse response = new BookListResponse();
        response.setBookResponses(books.stream().map(this::bookToResponse).collect(Collectors.toList()));
        return response;
    }

}
