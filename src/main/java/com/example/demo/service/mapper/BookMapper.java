package com.example.demo.service.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.model.Book;

@Component
public class BookMapper {

    public Book toEntity(BookRequestDTO dto) {
        var book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
        book.setIsbn(dto.getIsbn());
        return book;
    }

    public BookResponseDTO toDto(Book book) {
        var dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setISBN(book.getIsbn());
        return dto;
    }

    
}
