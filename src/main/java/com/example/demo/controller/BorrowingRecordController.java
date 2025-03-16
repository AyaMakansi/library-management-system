package com.example.demo.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BorrowingRecord.BorrowingRecordRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;
import com.example.demo.service.BorrowingRecordService;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController {

    @Autowired
    private final BorrowingRecordService borrowingRecordService;

    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordResponseDTO> borrowbook(@PathVariable UUID bookId, @PathVariable UUID patronId,
            @RequestBody @Valid BorrowingRecordRequestDTO request) {
        var createdBorrwingRecord = borrowingRecordService.borrowBook(bookId, patronId, request);
        return new ResponseEntity<>(createdBorrwingRecord, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordResponseDTO> returnbook(@PathVariable UUID bookId, @PathVariable UUID patronId,
             @RequestBody @Valid BorrowingRecordRequestDTO request) {
        try {
            // Call the service to record the book return
            BorrowingRecordResponseDTO updatedBorrowingRecord = borrowingRecordService.returnBook(bookId, patronId,
                    request);
            return new ResponseEntity<>(updatedBorrowingRecord, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
