package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;
import com.example.demo.exception.PatronNotFoundException;
import com.example.demo.model.Patron;
import com.example.demo.repository.PatronRepository;

import jakarta.transaction.Transactional;

@Service
public class PatronService {
    private static final Logger logger = LoggerFactory.getLogger(PatronService.class);

    @Autowired
    private PatronRepository _repository;

    @Transactional
    public List<PatronResponseDTO> getAllPatrons() {
        logger.info("Fetching all patrons");
        long start = System.currentTimeMillis();

        List<PatronResponseDTO> patrons = _repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Fetched {} patrons in {} ms", patrons.size(), elapsedTime);

        return patrons;
    }

    @Transactional
    public PatronResponseDTO getPatronById(UUID id) {
        logger.info("Fetching patron with ID: {}", id);
        return _repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.error("Patron not found with ID: {}", id);
                    return new PatronNotFoundException(id);
                });
    }

    @Transactional
    public PatronResponseDTO createPatron(PatronRequestDTO patronRequestDTO) {
        logger.info("Creating a new patron: {}", patronRequestDTO.getName());
        Patron patron = convertToEntity(patronRequestDTO);

        long start = System.currentTimeMillis();
        PatronResponseDTO response = convertToDTO(_repository.save(patron));
        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Patron created successfully with ID: {} in {} ms", response.getId(), elapsedTime);
        return response;
    }

    @Transactional
    public PatronResponseDTO updatePatron(UUID id, PatronRequestDTO patronRequestDTO) {
        logger.info("Updating patron with ID: {}", id);

        if (!_repository.existsById(id)) {
            logger.error("Update failed - Patron not found with ID: {}", id);
            throw new RuntimeException("Patron not found");
        }

        Patron patron = convertToEntity(patronRequestDTO);
        patron.setId(id);

        long start = System.currentTimeMillis();
        PatronResponseDTO response = convertToDTO(_repository.save(patron));
        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Patron updated successfully with ID: {} in {} ms", id, elapsedTime);
        return response;
    }

    @Transactional
    public boolean deletePatron(UUID id) {
        logger.info("Attempting to delete patron with ID: {}", id);

        var patron = _repository.findById(id);
        if (patron.isPresent()) {
            _repository.delete(patron.get());
            logger.info("Patron with ID: {} deleted successfully", id);
            return true;
        }

        logger.warn("Delete failed - Patron not found with ID: {}", id);
        return false;
    }

    private Patron convertToEntity(PatronRequestDTO dto) {
        Patron patron = new Patron();
        patron.setName(dto.getName());
        patron.setAddress(dto.getAddress());
        patron.setEmail(dto.getEmail());
        patron.setPhoneNumber(dto.getPhoneNumber());
        return patron;
    }

    private PatronResponseDTO convertToDTO(Patron patron) {
        PatronResponseDTO dto = new PatronResponseDTO();
        dto.setId(patron.getId());
        dto.setEmail(patron.getEmail());
        dto.setAddress(patron.getAddress());
        dto.setPhoneNumber(patron.getPhoneNumber());
        dto.setName(patron.getName());
        return dto;
    }
}
