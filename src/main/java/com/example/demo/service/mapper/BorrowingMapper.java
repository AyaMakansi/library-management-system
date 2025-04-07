package com.example.demo.service.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;
import com.example.demo.model.BorrowingRecord;

@Component
public class BorrowingMapper {

     public BorrowingRecordResponseDTO toDto(BorrowingRecord record) {
        return BorrowingRecordResponseDTO.builder()
                .Id(record.getId())
                .PatronId(record.getPatron() != null ? record.getPatron().getId() : null)
                .PatronName(record.getPatron() != null ? record.getPatron().getName() : "Unknown Patron")
                .BookId(record.getBook() != null ? record.getBook().getId() : null)
                .BookName(record.getBook() != null ? record.getBook().getTitle() : "Unknown Book")
                .borrowDate(record.getBorrowDate())
                .returnDate(record.getReturnDate())
                .isAvailable(record.getIsAvailable())
                .build();
    }
}
