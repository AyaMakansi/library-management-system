package com.example.demo.exception.BookExceptions;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Book not found with ID: %s";

    public BookNotFoundException(UUID id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
