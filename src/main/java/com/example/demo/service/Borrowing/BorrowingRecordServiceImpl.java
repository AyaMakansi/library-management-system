package com.example.demo.service.Borrowing;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BorrowingRecord.ReturnRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;
import com.example.demo.exception.BorrowingExceptions.InvalidReturnDateException;
import com.example.demo.exception.BorrowingExceptions.NotFoundException;
import com.example.demo.model.BorrowingRecord;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowingRecordRepository;
import com.example.demo.repository.PatronRepository;
import com.example.demo.service.mapper.BorrowingMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private BorrowingRecordRepository repository;
    private final BorrowingMapper mapper;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;

    @Transactional
    public BorrowingRecordResponseDTO borrowBook(UUID bookId, UUID patronId, BorrowRequestDTO request) {
        log.info("Attempting to borrow book with ID: {} for patron with ID: {} on date: {}", bookId, patronId,
                request.getBorrowDate());

        // Fetch book and patron entities
        var book = bookRepository.getReferenceById(bookId);
        var patron = patronRepository.getReferenceById(patronId);

        var record =new BorrowingRecord();
        // Create Borrowing Record
        record.BorrowBook(book,patron,LocalDate.now());

        // Save and return response DTO
        var response = mapper.toDto(repository.save(record));

        log.info("Book with ID: {} successfully borrowed by patron with ID: {}", bookId, patronId);

        return response;
    }

    @Transactional
    public BorrowingRecordResponseDTO returnBook(UUID bookId, UUID patronId, ReturnRequestDTO request) {
        log.info("Attempting to return book with ID: {} for patron with ID: {}", bookId, patronId);

        // Fetch the borrowing record
        var record = findBookOrPatronThrow(bookId, patronId);
        // Validate the return date
        validateReturnDate(record.getBorrowDate(), request.getReturnDate());

        // Update and save the return date
        record.ReturnBook(request.getReturnDate());
        repository.save(record);

        // Map the record to response DTO
        BorrowingRecordResponseDTO response = mapper.toDto(record);

        log.info("Book with ID: {} successfully returned by patron with ID: {}", bookId, patronId);
        return response;
    }

    // Validate the return date
    private void validateReturnDate(LocalDate borrowDate, LocalDate returnDate) {
        if (returnDate.isBefore(borrowDate)) {
            throw new InvalidReturnDateException();
        }
    }

    private BorrowingRecord findBookOrPatronThrow(UUID bookId, UUID patronId) {
        return repository.findByBook_IdAndPatron_Id(bookId, patronId)
                .orElseThrow(() -> {
                    log.error("Borrowing record not found for Book ID: " + bookId + " and Patron ID: " + patronId);
                    return new NotFoundException(bookId, patronId);
                });
    }
}
