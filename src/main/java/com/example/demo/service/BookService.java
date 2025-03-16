package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository _repository;

   
 public List<BookResponseDTO> getAllBooks() {
        return _repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBookById(UUID id) {
       return _repository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new BookNotFoundException(id));
    }

    public BookResponseDTO createBook( BookRequestDTO bookRequestDTO) {
        Book book = convertToEntity(bookRequestDTO);
        return convertToDTO(_repository.save(book));
    }

    public BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        if (!_repository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        Book book = convertToEntity(bookRequestDTO);
        book.setId(id);
        return convertToDTO(_repository.save(book));
    }

    public boolean deleteBook(UUID id) {
        var book = _repository.findById(id);  // Find the book by ID
        if (book.isPresent()) {  // If the book exists
            _repository.delete(book.get());  // Delete the book
            return true;  // Book deleted successfully
        }
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
