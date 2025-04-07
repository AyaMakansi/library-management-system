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

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;
import com.example.demo.service.Patrons.PatronService;

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
@Tag(name = "Patron")
@RequestMapping("/api/patrons")
public class PatronController {

    private PatronService patronService;


    @Operation(summary = "Get all patrons", description = "Fetches a list of all patrons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patrons", content = @Content(schema = @Schema(implementation = PatronResponseDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "No patrons found")
    })
    @GetMapping
    public ResponseEntity<List<PatronResponseDTO>> getAllPatrons() {
        var patrons = patronService.getAllPatrons();
        return patrons.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(patrons);
    }

    //@Cacheable
    @Operation(summary = "Get patron by ID", description = "Fetches a patron's details by their unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved patron", content = @Content(schema = @Schema(implementation = PatronResponseDTO.class), mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<PatronResponseDTO> getPatronById(@PathVariable UUID id) {
        return ResponseEntity.ok(patronService.getPatronById(id));
    }

    @Operation(summary = "Create a new patron", description = "Creates a new patron based on the provided request data")
    @ApiResponse(responseCode = "201", description = "Successfully created patron", content = @Content(schema = @Schema(implementation = PatronResponseDTO.class), mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<PatronResponseDTO> createPatron(@Valid @RequestBody PatronRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patronService.createPatron(request));
    }

    @Operation(summary = "Update patron by ID", description = "Updates an existing patron's details based on the provided request data")
    @ApiResponse(responseCode = "200", description = "Successfully updated patron", content = @Content(schema = @Schema(implementation = PatronResponseDTO.class), mediaType = "application/json"))
    @PutMapping("/{id}")
    public ResponseEntity<PatronResponseDTO> updatePatron(@PathVariable UUID id,
            @Valid @RequestBody PatronRequestDTO request) {
        return ResponseEntity.ok(patronService.updatePatron(id, request));
    }

    @Operation(summary = "Delete patron by ID", description = "Deletes a patron by their unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted patron")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable UUID id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }

    
}
