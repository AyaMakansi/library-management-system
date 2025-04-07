package com.example.demo.service.Borrowing;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BorrowingRecord.ReturnRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;

@Service
public interface BorrowingRecordService  {

    public BorrowingRecordResponseDTO borrowBook(UUID bookId, UUID patronId, BorrowRequestDTO request);
    public BorrowingRecordResponseDTO returnBook(UUID bookId, UUID patronId, ReturnRequestDTO request);
}
