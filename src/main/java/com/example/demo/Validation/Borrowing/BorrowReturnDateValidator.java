package com.example.demo.Validation.Borrowing;

import java.time.LocalDate;

import com.example.demo.dto.BorrowingRecord.BorrowReturnRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Validator
public class BorrowReturnDateValidator implements ConstraintValidator<ValidBorrowReturnDates, BorrowReturnRequestDTO> {

    @Override
    public boolean isValid(BorrowReturnRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;  // Handle null case if needed
        }
        LocalDate borrowDate = dto.getBorrowDate();
        LocalDate returnDate = dto.getReturnDate();

        if (borrowDate == null || returnDate == null) {
            return true;  // Handle null date case if needed
        }

        return returnDate.isAfter(borrowDate); // Ensure returnDate is after borrowDate
    }
}
