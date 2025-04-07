package com.example.demo.service.Book;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.exception.BookExceptions.BookNotFoundException;

@Service
public interface BookService {

     public List<BookResponseDTO> getAllBooks();
     public BookResponseDTO getBookById(UUID id) throws BookNotFoundException;
      public BookResponseDTO createBook( BookRequestDTO bookRequestDTO);
      public BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO);
      public void deleteBook(UUID id);
      
}
