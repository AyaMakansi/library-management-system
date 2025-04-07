package com.example.demo.exception.BorrowingExceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Borrowing record not found for Book ID: %s and Patron ID: %s";

    public NotFoundException(UUID bookId, UUID patronId) {
        super(String.format(MESSAGE_TEMPLATE, bookId, patronId));
    }
 }
