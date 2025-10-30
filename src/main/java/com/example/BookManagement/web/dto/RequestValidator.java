package com.example.BookManagement.web.dto;

public class RequestValidator {

    public void validate(BookRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateNotBlank(request.title(), "Название книги");
        validateNotBlank(request.author(), "Автор");
        validateNotBlank(request.category(), "Категория");

        validateLength(request.title(), "Название книги", 1, 255);
        validateLength(request.author(), "Автор", 1, 255);
        validateLength(request.category(), "Категория", 1, 100);
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private void validateLength(String value, String fieldName, int min, int max) {
        if (value.length() < min) {
            throw new IllegalArgumentException(fieldName + " должно содержать минимум " + min + " символов");
        }
        if (value.length() > max) {
            throw new IllegalArgumentException(fieldName + " не должно превышать " + max + " символов");
        }
    }
}