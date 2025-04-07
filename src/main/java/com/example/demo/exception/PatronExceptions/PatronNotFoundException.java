package com.example.demo.exception.PatronExceptions;

import java.util.UUID;

public class PatronNotFoundException extends RuntimeException {

   private static final String MESSAGE_TEMPLATE = "Patron not found with ID: %s";

    public PatronNotFoundException(UUID id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
