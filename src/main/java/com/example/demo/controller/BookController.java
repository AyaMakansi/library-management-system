package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;
import com.example.demo.service.Book.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Tag(name = "Book")
@RequestMapping("/api/books")
public class BookController {

    private BookService BookService;

    @Operation(summary = "Get all Books", description = "Fetches a list of all Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Books", content = @Content(schema = @Schema(implementation = BookResponseDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "No Books found")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        var Books = BookService.getAllBooks();
        return Books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(Books);
    }

   // @Cacheable
    @Operation(summary = "Get Book by ID", description = "Fetches a Book's details by their unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved Book", content = @Content(schema = @Schema(implementation = BookResponseDTO.class), mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(BookService.getBookById(id));
    }

    @Operation(summary = "Create a new Book", description = "Creates a new Book based on the provided request data")
    @ApiResponse(responseCode = "201", description = "Successfully created Book", content = @Content(schema = @Schema(implementation = BookResponseDTO.class), mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BookService.createBook(request));
    }

    @Operation(summary = "Update Book by ID", description = "Updates an existing Book's details based on the provided request data")
    @ApiResponse(responseCode = "200", description = "Successfully updated Book", content = @Content(schema = @Schema(implementation = BookResponseDTO.class), mediaType = "application/json"))
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable UUID id,
            @Valid @RequestBody BookRequestDTO request) {
        return ResponseEntity.ok(BookService.updateBook(id, request));
    }

    @Operation(summary = "Delete Book by ID", description = "Deletes a Book by their unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted Book")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        BookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
