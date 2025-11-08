package com.example.BookManagement.repository;

import com.example.BookManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByAuthorAndTitle(String author, String title);

    List<Book> findByCategoryId(Long categoryId);
}
