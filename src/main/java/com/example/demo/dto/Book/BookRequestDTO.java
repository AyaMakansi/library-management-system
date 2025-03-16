package com.example.demo.dto.Book;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 100 characters")
    private String title;
    @NotNull(message = "Author cannot be null")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String author;
    @NotNull(message = "PublicationYear cannot be null")
    @DateTimeFormat(pattern = "yyyy")
    private LocalDate  publicationYear;
    @Null
    @Pattern(regexp = "^(\\d{13})$", message = "ISBN must be a 13-digit number")
    private String isbn;
}
