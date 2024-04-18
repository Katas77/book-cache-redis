package com.example.BookManagement.repository;

import com.example.BookManagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByCategory(String category);
}
