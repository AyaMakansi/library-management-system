package com.example.demo.dto.BorrowingRecord;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowReturnRequestDTO {

    @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;
    @NotNull(message = "Return date cannot be null")
    private LocalDate returnDate;


}
