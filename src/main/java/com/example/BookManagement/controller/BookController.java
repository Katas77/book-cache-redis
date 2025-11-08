package com.example.BookManagement.controller;

import com.example.BookManagement.redisDumper.RedisDumper;
import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.web.Mapper;
import com.example.BookManagement.web.dto.BookRequest;
import com.example.BookManagement.web.dto.BookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final Mapper mapper;
    private final BookInterface service;
    private final RedisDumper dumper;

    @GetMapping("/{title}/{author}")
    public BookResponse findTitleAndAuthor(@PathVariable String title, @PathVariable String author) {
    dumper.dumpAllToConsole();
        return mapper.toResponse(service.findByTitle(title, author));
    }

    @GetMapping("/{category}")
    public List <BookResponse> findCategory(@PathVariable String category) {
        return mapper.toBookResponseList(service.findCategory(category));
    }

    @PostMapping
    public String create(@RequestBody BookRequest request) {
              return MessageFormat.format("Книга с названием {0} сохранена", service.save(mapper.requestToBook(request)).getTitle());
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody BookRequest request) {
        return service.update(id,mapper.requestToBook(request));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.deleteById(id);
    }
}

