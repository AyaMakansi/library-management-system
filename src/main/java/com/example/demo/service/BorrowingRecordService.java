package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BorrowingRecordService.class);

    @Autowired
    private BorrowingRecordRepository _repository;
    @Autowired
    private BookRepository _booRepo;
    @Autowired
    private PatronRepository _patronRepo;

    @Transactional
    public BorrowingRecordResponseDTO borrowBook(UUID bookId, UUID patronId, BorrowingRecordRequestDTO request) {
        logger.info("Attempting to borrow book with ID: {} for patron with ID: {}", bookId, patronId);

        var book = _booRepo.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", bookId);
                    return new BookNotFoundException(bookId);
                });

        var patron = _patronRepo.findById(patronId)
                .orElseThrow(() -> {
                    logger.error("Patron not found with ID: {}", patronId);
                    return new PatronNotFoundException(patronId);
                });

        // Create Borrowing Record
        var borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(request.getBorrowDate());

        // Save and return response DTO
        BorrowingRecordResponseDTO response = convertToDTO(_repository.save(borrowingRecord));
        logger.info("Book with ID: {} successfully borrowed by patron with ID: {}", bookId, patronId);
        return response;
    }

    @Transactional
    public BorrowingRecordResponseDTO returnBook(UUID bookId, UUID patronId, BorrowingRecordRequestDTO request) {
        logger.info("Attempting to return book with ID: {} for patron with ID: {}", bookId, patronId);

        Optional<BorrowingRecord> recordOptional = _repository.findByBookIdAndPatronId(bookId, patronId);

        if (recordOptional.isPresent()) {
            BorrowingRecord record = recordOptional.get();
            record.setReturnDate(request.getReturnDate());

            _repository.save(record);

            BorrowingRecordResponseDTO response = new BorrowingRecordResponseDTO();
            response.setBookName(record.getBook() != null ? record.getBook().getTitle() : "Unknown Book");
            response.setPatronName(record.getPatron() != null ? record.getPatron().getName() : "Unknown Patron");
            response.setBookId(record.getBook().getId());
            response.setPatronId(record.getPatron().getId());
            response.setReturnDate(record.getReturnDate());

            logger.info("Book with ID: {} successfully returned by patron with ID: {}", bookId, patronId);
            return response;
        } else {
            logger.warn("No active borrowing record found for book ID: {} and patron ID: {}", bookId, patronId);
            return new BorrowingRecordResponseDTO(); // No active borrowing record found
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
