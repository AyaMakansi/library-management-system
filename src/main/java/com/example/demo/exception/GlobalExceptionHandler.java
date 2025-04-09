package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.BookExceptions.BookNotFoundException;
import com.example.demo.exception.BookExceptions.DuplicateIsbnException;
import com.example.demo.exception.BorrowingExceptions.InvalidReturnDateException;
import com.example.demo.exception.BorrowingExceptions.NotFoundException;
import com.example.demo.exception.PatronExceptions.PatronNotFoundException;

import lombok.var;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        var errorResponse = new ErrorResponse(errors, LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        var errorResponse = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PatronNotFoundException.class)
    public ResponseEntity<Object> handlePatronNotFoundException(PatronNotFoundException ex) {
        var errorResponse = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidReturnDateException.class)
    public ResponseEntity<Object> handleInvalidReturnDate(InvalidReturnDateException ex) {
        var errorResponse = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        var errorResponse = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<Object> handleDuplicateIsbnException(DuplicateIsbnException ex) {
        var errorResponse = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(messages, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(messages, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  
}
