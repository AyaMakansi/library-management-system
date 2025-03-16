package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BorrowingRecord.BorrowingRecordRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.PatronNotFoundException;
import com.example.demo.model.BorrowingRecord;

import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowingRecordRepository;
import com.example.demo.repository.PatronRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowingRecordService {
    @Autowired
    private BorrowingRecordRepository _repository;
    @Autowired
    private BookRepository _booRepo;
    @Autowired
    private PatronRepository _patronRepo;
    @Transactional
    public BorrowingRecordResponseDTO borrowBook(UUID bookId, UUID patronId, BorrowingRecordRequestDTO request) {

        var book = _booRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        var patron = _patronRepo.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException(patronId));

        // Create Borrowing Record
        var borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(request.getBorrowDate());

        // Save and return response DTO
        return convertToDTO(_repository.save(borrowingRecord));
    }

    @Transactional
    public BorrowingRecordResponseDTO returnBook(UUID bookId, UUID patronId, BorrowingRecordRequestDTO request) {
        // Find the borrowing record for the book and patron
        Optional<BorrowingRecord> recordOptional = _repository.findByBookIdAndPatronId(bookId, patronId);

        if (recordOptional.isPresent()) {
            BorrowingRecord record = recordOptional.get();

            // Set the return date to the current date or the provided return date
            record.setReturnDate(request.getReturnDate());

            // Save the updated borrowing record
            _repository.save(record);

            // Create a response DTO to return the success status and the updated record
            // details
            BorrowingRecordResponseDTO response = new BorrowingRecordResponseDTO();

            if (record.getBook() != null) {
                response.setBookName(record.getBook().getTitle());
            } else {
                response.setBookName("Unknown Book");
            }

            if (record.getPatron() != null) {
                response.setPatronName(record.getPatron().getName());
            } else {
                response.setPatronName("Unknown Patron");
            }

            response.setBookId(record.getBook().getId());
            response.setPatronId(record.getPatron().getId());
            response.setReturnDate(record.getReturnDate());

            return response; // Successfully returned
        } else {
            // Handle the case where no borrowing record is found
            BorrowingRecordResponseDTO response = new BorrowingRecordResponseDTO();

            return response; // No active borrowing record found
        }
    }

    private BorrowingRecordResponseDTO convertToDTO(BorrowingRecord borrowingRecord) {
        var dto = new BorrowingRecordResponseDTO();

        dto.setId(borrowingRecord.getId());
        dto.setBorrowDate(borrowingRecord.getBorrowDate());
        dto.setReturnDate(borrowingRecord.getReturnDate());
        dto.setBookId(borrowingRecord.getBook().getId());
        dto.setPatronId(borrowingRecord.getPatron().getId());
        dto.setBookName(borrowingRecord.getBook().getTitle());
        dto.setPatronName(borrowingRecord.getPatron().getName());
        return dto;
    }

}
