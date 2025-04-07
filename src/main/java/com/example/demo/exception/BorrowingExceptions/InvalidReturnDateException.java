package com.example.demo.exception.BorrowingExceptions;

public class InvalidReturnDateException extends RuntimeException {

   private static final String MESSAGE_TEMPLATE = "Return date cannot be before the borrow date.";

    public InvalidReturnDateException() {
        super(String.format(MESSAGE_TEMPLATE));}

}
