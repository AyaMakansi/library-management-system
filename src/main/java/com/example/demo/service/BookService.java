package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository _repository;

    @Transactional
    public List<BookResponseDTO> getAllBooks() {
        logger.info("Fetching all books");
        long start = System.currentTimeMillis();

        List<BookResponseDTO> books = _repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Fetched {} books in {} ms", books.size(), elapsedTime);

        return books;
    }

    @Transactional
    public BookResponseDTO getBookById(UUID id) {
        logger.info("Fetching book with ID: {}", id);
        return _repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new BookNotFoundException(id);
                });
    }

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        logger.info("Creating a new book: {}", bookRequestDTO.getTitle());
        Book book = convertToEntity(bookRequestDTO);

        long start = System.currentTimeMillis();
        BookResponseDTO response = convertToDTO(_repository.save(book));
        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Book created successfully with ID: {} in {} ms", response.getId(), elapsedTime);
        return response;
    }

    @Transactional
    public BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        logger.info("Updating book with ID: {}", id);

        if (!_repository.existsById(id)) {
            logger.error("Update failed - Book not found with ID: {}", id);
            throw new RuntimeException("Book not found");
        }

        Book book = convertToEntity(bookRequestDTO);
        book.setId(id);

        long start = System.currentTimeMillis();
        BookResponseDTO response = convertToDTO(_repository.save(book));
        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Book updated successfully with ID: {} in {} ms", id, elapsedTime);
        return response;
    }

    @Transactional
    public boolean deleteBook(UUID id) {
        logger.info("Attempting to delete book with ID: {}", id);

        var book = _repository.findById(id);
        if (book.isPresent()) {
            _repository.delete(book.get());
            logger.info("Book with ID: {} deleted successfully", id);
            return true;
        }

        logger.warn("Delete failed - Book not found with ID: {}", id);
        return false;
    }

    private Book convertToEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
        book.setISBN(dto.getIsbn());
        return book;
    }

    private BookResponseDTO convertToDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setISBN(book.getISBN());
        return dto;
    }
}
