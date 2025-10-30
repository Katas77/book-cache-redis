package com.example.BookManagement.controller;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.redisDumper.RedisDumper;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.web.dto.BookListResponse;
import com.example.BookManagement.web.dto.BookRequest;
import com.example.BookManagement.web.dto.BookResponse;
import com.example.BookManagement.web.mapper.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final Mapper mapper;
    private final BookInterface service;
    private final RedisDumper redisDumper;

    @GetMapping("/{title}/{author}")
    public BookResponse findTitleAndAuthor(@PathVariable String title, @PathVariable String author) {
        redisDumper.dumpAllToConsole();
        return mapper.bookToResponse(service.findByTitle(title, author));
    }

    @GetMapping("/{category}")
    public BookListResponse findCategory(@PathVariable String category) {
        return mapper.bookListResponseList(service.findCategory(category));
    }

    @PostMapping
    public ResponseEntity<String>create(@RequestBody @Valid BookRequest request) {
        return ResponseEntity.ok(MessageFormat.format("Книга с названием {0} сохранена", service.save(mapper.requestToBook(request), mapper.requestToCategory(request)).getId()));
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
        return service.update(mapper.requestToBook(id, request), mapper.requestToCategory(request));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.deleteById(id);
    }
}

