package com.example.demo.service.Book;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.exception.BookExceptions.BookNotFoundException;
import com.example.demo.exception.BookExceptions.DuplicateIsbnException;
import com.example.demo.exception.PatronExceptions.PatronNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.mapper.BookMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService{
    private BookRepository repository;

    private BookMapper mapper;
    

    public List<BookResponseDTO> getAllBooks() {
        log.info("Fetching all books");
        var start = System.currentTimeMillis();

        var books = repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        var elapsedTime = System.currentTimeMillis() - start;
        log.info("Fetched {} books in {} ms", books.size(), elapsedTime);

        return books;
    }

    
    //@Cacheable(value = "books", key = "#id") // Cache book by ID
    public BookResponseDTO getBookById(UUID id) throws BookNotFoundException{
        log.info("Fetching book with ID: {}", id);
        var book = findBookOrThrow(id);
        return mapper.toDto(book);
    }

    @Transactional
    public BookResponseDTO createBook( BookRequestDTO bookRequestDTO) {
       
        log.info("Creating a new book: {}", bookRequestDTO.getTitle());
        var book = mapper.toEntity(bookRequestDTO);

        if (repository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        var start = System.currentTimeMillis();
        var response = mapper.toDto(repository.save(book));
        var elapsedTime = System.currentTimeMillis() - start;

        log.info("Book created successfully with ID: {} in {} ms", response.getId(), elapsedTime);
        return response;
    }

    @Transactional
    public BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        log.info("Updating book with ID: {}", id);

        var book = findBookOrThrow(id);
        book.setTitle(bookRequestDTO.getTitle());
        book.setAuthor(bookRequestDTO.getAuthor());
        book.setIsbn(bookRequestDTO.getIsbn());
        book.setPublicationYear(bookRequestDTO.getPublicationYear());

        var start = System.currentTimeMillis();
        var response = mapper.toDto(repository.save(book));
        var elapsedTime = System.currentTimeMillis() - start;

        log.info("Book updated successfully with ID: {} in {} ms", id, elapsedTime);
        return response;
    }
   @Transactional
    public void deleteBook(UUID id) {
        log.info("Attempting to delete book with ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Delete failed - Book not found with ID: {}", id);
            throw new PatronNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Book with ID: {} deleted successfully", id);
    }

     public Book findBookOrThrow(UUID id) {
        return repository.findById(id)
        .orElseThrow(() -> {
            log.error("Book not found with ID: {}", id);
            return new BookNotFoundException(id);  // A custom exception indicating a not-found book
        });
    }
    
}
