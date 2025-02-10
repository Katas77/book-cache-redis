package com.example.BookManagement.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse implements Serializable {
    private Long id;
    private String author;
    private String title;
    private String category;


}
