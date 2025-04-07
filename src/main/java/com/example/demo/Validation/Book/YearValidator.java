package com.example.demo.Validation.Book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        
        return value>= 1000 && value<= 9999;
    }
}
