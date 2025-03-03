package com.example.BookManagement.repository;

import com.example.BookManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByAuthorAndTitle(String author, String title);
    List<Optional<Book>> findByCategoryId(long categoryId);
}


