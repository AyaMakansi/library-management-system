package com.example.demo.dto.BorrowingRecord;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRequestDTO {
    
   @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;
}
