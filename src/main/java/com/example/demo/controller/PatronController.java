package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.service.PatronService;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private final PatronService PatronService;

    public PatronController(PatronService PatronService) {
        this.PatronService = PatronService;
    }

    @GetMapping
    public List<PatronResponseDTO> getAllPatrons() {
        return PatronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronResponseDTO> getPatronById(@PathVariable UUID id) {
        var Patron = PatronService.getPatronById(id);
        if (Patron == null) {
            return ResponseEntity.notFound().build(); // Corrected condition
        }
        return ResponseEntity.ok(Patron);

    }

    @PostMapping
    public ResponseEntity<PatronResponseDTO> createPatron(@RequestBody @Valid PatronRequestDTO request) {
        var createdPatron = PatronService.createPatron(request);
        return new ResponseEntity<>(createdPatron, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronResponseDTO> updatePatron(@PathVariable UUID id,
            @Valid @RequestBody PatronRequestDTO request) {
        try {
            var updatedPatron = PatronService.updatePatron(id, request);
            return new ResponseEntity<>(updatedPatron, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatron(@PathVariable UUID id) {

        var isDeleted = PatronService.deletePatron(id);
        // If the Patron is successfully deleted, return 200 OK with a success message.
        if (isDeleted) {
            return ResponseEntity.ok("Patron successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Patron not found with ID: " + id);
        }
    }
}
