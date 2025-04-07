package com.example.demo.dto.Book;



import com.example.demo.Validation.Book.ValidYear;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Nullable
    @ValidYear
    private Integer  publicationYear;
    @Column(unique = true, nullable = false, length = 13)
    @Pattern(regexp = "^(\\d{13})$", message = "ISBN must be a 13-digit number")
    private String isbn;
}
