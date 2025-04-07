package com.example.demo.exception;


public class EmailAlreadyExistsException 
extends RuntimeException {

   private static final String MESSAGE_TEMPLATE = "Email %s not found with ID: %s";

    public EmailAlreadyExistsException(String email) {
        super(String.format(MESSAGE_TEMPLATE, email));
    }
}
