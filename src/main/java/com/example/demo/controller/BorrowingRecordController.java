package com.example.demo.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BorrowingRecord.ReturnRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;
import com.example.demo.service.Borrowing.BorrowingRecordService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Tag(name = "Borrowing Record")
@RequestMapping("/api")
public class BorrowingRecordController {

    private  BorrowingRecordService borrowingRecordService;


    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordResponseDTO> borrowbook(@PathVariable UUID bookId, @PathVariable UUID patronId,
            @RequestBody @Valid BorrowRequestDTO request) {
        var createdBorrwingRecord = borrowingRecordService.borrowBook(bookId, patronId, request);
        return new ResponseEntity<>(createdBorrwingRecord, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordResponseDTO> returnbook(@PathVariable UUID bookId, @PathVariable UUID patronId,
             @RequestBody @Valid ReturnRequestDTO request) {
         var updatedBorrowingRecord = borrowingRecordService.returnBook(bookId, patronId, request);
            return new ResponseEntity<>(updatedBorrowingRecord, HttpStatus.OK);
    }
}
