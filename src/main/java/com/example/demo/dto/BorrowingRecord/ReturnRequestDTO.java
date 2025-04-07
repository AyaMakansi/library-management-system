package com.example.demo.dto.BorrowingRecord;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequestDTO {

    @NotNull(message = "Return date cannot be null")
    private LocalDate returnDate;
}
