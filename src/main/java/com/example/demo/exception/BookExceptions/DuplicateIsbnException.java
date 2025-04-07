package com.example.demo.exception.BookExceptions;

public class DuplicateIsbnException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "A book with Isbn  %s  already exists.";

    public DuplicateIsbnException(String isbn) {
        super(String.format(MESSAGE_TEMPLATE, isbn));
    }
}
