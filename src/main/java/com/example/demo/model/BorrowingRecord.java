package com.example.demo.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrowingRecords")
@Builder
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookId")  // Foreign key to Book
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patronId")  // Foreign key to Patron
    private Patron patron;

    private LocalDate borrowDate;
    private LocalDate returnDate;

     // Custom Getters for bookId and patronId
     public UUID getBookId() {
        return (book != null) ? book.getId() : null;
    }

    public UUID getPatronId() {
        return (patron != null) ? patron.getId() : null;
    }

    // Custom Setters for bookId and patronId
    public void setBookById(UUID bookId) {
        this.book = new Book();
        this.book.setId(bookId);
    }

    public void setPatronById(UUID patronId) {
        this.patron = new Patron();
        this.patron.setId(patronId);
    }
}
