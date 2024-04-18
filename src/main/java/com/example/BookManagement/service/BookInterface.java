package com.example.BookManagement.service;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface BookInterface {

    Book findByTitle(String title, String author);

    List<Book> findCategory(String category);

    Book findById(Long id);

    ResponseEntity<String> save(Book book, Category category);

    ResponseEntity<String> update(Book book, Category category);

    ResponseEntity<String> deleteById(Long id);


}
