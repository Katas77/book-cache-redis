package com.example.BookManagement.exception;

public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException () {
    }
    public BusinessLogicException (Exception cause) {
        super(cause);
    }

    public BusinessLogicException (String message) {
        super(message);
    }

    public BusinessLogicException (String message, Exception cause) {
        super(message, cause);
    }

}