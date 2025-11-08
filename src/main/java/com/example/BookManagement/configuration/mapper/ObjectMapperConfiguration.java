package com.example.BookManagement.configuration.mapper;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ObjectMapperConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.addMixIn(Book.class, BookMixin.class);
        mapper.addMixIn(Category.class, CategoryMixin.class);
        return mapper;
    }

    // Миксины для контроля сериализации
    abstract class BookMixin {
        @JsonIgnore
        abstract Category getCategory(); // или @JsonManagedReference, но проще игнорировать при дампе
    }

    abstract class CategoryMixin {
        @JsonIgnore
        abstract List<Book> getBooks();
    }
}

