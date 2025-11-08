package com.example.BookManagement.service.jackson_book;

public record BookDto(
        Long id,
        String title,
        String author,
        String categoryName
) {}