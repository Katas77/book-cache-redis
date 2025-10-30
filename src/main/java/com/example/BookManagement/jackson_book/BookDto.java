package com.example.BookManagement.jackson_book;

public record BookDto(
        Long id,
        String title,
        String author,
        String categoryName
) {}