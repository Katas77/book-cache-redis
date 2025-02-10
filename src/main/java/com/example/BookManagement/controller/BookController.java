
package com.example.BookManagement.controller;


import com.example.BookManagement.service.BookInterface;
import com.example.BookManagement.web.dto.BookListResponse;
import com.example.BookManagement.web.dto.BookRequest;
import com.example.BookManagement.web.dto.BookResponse;
import com.example.BookManagement.web.mapper.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final Mapper mapper;
    private final BookInterface service;


    @GetMapping("/{title}/{author}")
    public BookResponse findTitleAndAuthor(@PathVariable String title, @PathVariable String author) {
        return mapper.bookToResponse(service.findByTitle(title, author));
    }

    @GetMapping("/{category}")
    public BookListResponse findCategory(@PathVariable String category) {
        return mapper.bookListResponseList(service.findCategory(category));
    }


    @PostMapping
    public String create(@RequestBody @Valid BookRequest request) {
        return service.save(mapper.requestToBook(request), mapper.requestToCategory(request));
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

