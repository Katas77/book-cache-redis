package com.example.BookManagement.repository;

import com.example.BookManagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategory(String category);

    boolean existsByCategory(String category);

}
