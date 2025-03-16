package com.example.demo.dto.Book;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    private UUID Id; 
    private String Title;
    private String Author;
    private LocalDate  PublicationYear;
    private String ISBN;
  
    
}
