package com.example.demo.Validation.Book;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = YearValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {
    String message() default "Invalid year format (must be YYYY)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
