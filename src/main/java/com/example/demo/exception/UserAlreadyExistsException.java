package com.example.demo.exception;


public class UserAlreadyExistsException extends RuntimeException {

   private static final String MESSAGE_TEMPLATE = "Email %s is already in use.";

    public UserAlreadyExistsException(String username) {
        super(String.format(MESSAGE_TEMPLATE, username));
    }
}
