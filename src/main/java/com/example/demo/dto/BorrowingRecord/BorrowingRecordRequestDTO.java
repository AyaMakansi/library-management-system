package com.example.demo.dto.BorrowingRecord;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.example.demo.service.ValidBorrowReturnDates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidBorrowReturnDates
public class BorrowingRecordRequestDTO {

   @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;

    @NotNull(message = "Return date cannot be null")
    private LocalDate returnDate;
}
