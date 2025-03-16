package com.example.demo.service;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.dto.BorrowingRecord.BorrowingRecordRequestDTO;

// Validator
public class BorrowReturnDateValidator implements ConstraintValidator<ValidBorrowReturnDates, BorrowingRecordRequestDTO> {

    @Override
    public boolean isValid(BorrowingRecordRequestDTO borrowRequestDTO, ConstraintValidatorContext context) {
        if (borrowRequestDTO == null) {
            return true;  // Handle null case if needed
        }
        LocalDate borrowDate = borrowRequestDTO.getBorrowDate();
        LocalDate returnDate = borrowRequestDTO.getReturnDate();

        if (borrowDate == null || returnDate == null) {
            return true;  // Handle null date case if needed
        }

        return returnDate.isAfter(borrowDate); // Ensure returnDate is after borrowDate
    }
}
