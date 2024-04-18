package com.example.BookManagement.repository;


import com.example.BookManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByAuthor(String author);

    List<Book> findByCategory_id(Long category_id);
}


