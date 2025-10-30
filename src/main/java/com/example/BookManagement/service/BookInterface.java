package com.example.BookManagement.service;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;


import java.util.List;

public interface BookInterface {

    Book findByTitle(String title, String author);

    List<Book> findCategory(String category);

    Book findById(Long id);

    Book save(Book book, Category category);

    String update(Book book, Category category);

    String deleteById(Long id);

}
