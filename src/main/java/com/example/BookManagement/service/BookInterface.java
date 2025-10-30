package com.example.BookManagement.service;

import com.example.BookManagement.model.Book;



import java.util.List;

public interface BookInterface {

    Book findByTitle(String title, String author);

    List<Book> findCategory(String category);

    Book findById(Long id);

    Book save(Book book);

    String update( Long id,Book book);

    String deleteById(Long id);

}
