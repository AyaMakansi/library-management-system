package com.example.demo.model;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "borrowingRecords")
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecord extends BaseEntity{
   
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "Book_Id", referencedColumnName = "id", nullable = false) // Foreign key to Book
    private Book book;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "Patron_Id", referencedColumnName = "id", nullable = false) // Foreign key to Patron
    private Patron patron;

    @Column(name = "borrowDate", nullable = true)
    private LocalDate borrowDate;

    @Column(name = "returnDate", nullable = true)
    private LocalDate returnDate;
    

    @Column(name = "isAvailable")
    private Boolean IsAvailable;

    // Custom Getters for bookId and patronId
    public UUID getBookId() {
        return (book != null) ? book.getId() : null;
    }

    public UUID getPatronId() {
        return (patron != null) ? patron.getId() : null;
    }


    public void BorrowBook(Book book, Patron patron, LocalDate borrowDate)
    {
        this.book = book;
        this.patron = patron;
        this.borrowDate = borrowDate;
        IsAvailable=false;
    }
    public void ReturnBook(LocalDate returnDate)
    {
       this.returnDate = returnDate;
        IsAvailable=true;
    }


}
