package com.example.demo.Validation.Borrowing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;



@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BorrowReturnDateValidator.class)
public @interface ValidBorrowReturnDates {
    String message() default "Return date must be after borrow date";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
