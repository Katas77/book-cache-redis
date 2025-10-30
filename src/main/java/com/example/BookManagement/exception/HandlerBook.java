package com.example.BookManagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class HandlerBook {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleException(BusinessLogicException e) {
        log.error("BusinessLogicException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException  .class)
    public ResponseEntity<String> handleExceptionNull(NullPointerException  e) {
        log.error("NullPointerException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body((e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException .class)
    public ResponseEntity<String> handleIllegalArgumentException (IllegalArgumentException  e) {
        log.error("IllegalArgumentException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body((e.getMessage()));
    }
}

