package com.example.BookManagement.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    @NotBlank(message = "название книги должно быть заполнено!")
    private String title;
    @NotBlank(message = "ФИО автора книги должно быть заполнено!")
    private String author;
    @NotBlank(message = "Категория  должна быть заполнена!")
    private String category;
}




