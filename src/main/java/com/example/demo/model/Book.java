package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "books")
@NoArgsConstructor
public class Book extends BaseEntity{

    @NotNull
    @Column(name = "title",nullable = false)
    private String title;

    @NonNull
    @Column(name = "author",nullable = false)
    private String author;


    @Column(name = "publicationYear",nullable = true)
    private Integer  publicationYear;



    @Column(name = "isbn",unique = true, nullable = false, length = 13)
    private String isbn;


    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords;

    public Book(String title, String author,Integer publicationYear,String isbn) {
        super();
        this.title=title;
        this.author=author;
        this.publicationYear=publicationYear;
        this.isbn=isbn;
    }
    
}
