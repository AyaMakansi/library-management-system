package com.example.demo.dto.BorrowingRecord;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordResponseDTO {

    private UUID Id;
    private UUID PatronId;
    private String PatronName;
    private UUID BookId;
    private String BookName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
}
