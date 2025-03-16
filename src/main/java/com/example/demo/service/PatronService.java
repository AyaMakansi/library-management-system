package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private PatronRepository _repository;

    @Transactional
 public List<PatronResponseDTO> getAllPatrons() {
        return _repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public PatronResponseDTO getPatronById(UUID id) {
       return _repository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new PatronNotFoundException(id));
    }
    @Transactional
    public PatronResponseDTO createPatron( PatronRequestDTO PatronRequestDTO) {
        Patron Patron = convertToEntity(PatronRequestDTO);
        return convertToDTO(_repository.save(Patron));
    }
    @Transactional
    public PatronResponseDTO updatePatron(UUID id, PatronRequestDTO PatronRequestDTO) {
        if (!_repository.existsById(id)) {
            throw new RuntimeException("Patron not found");
        }
        Patron Patron = convertToEntity(PatronRequestDTO);
        Patron.setId(id);
        return convertToDTO(_repository.save(Patron));
    }
    @Transactional
    public boolean deletePatron(UUID id) {
        var Patron = _repository.findById(id);  // Find the Patron by ID
        if (Patron.isPresent()) {  // If the Patron exists
            _repository.delete(Patron.get());  // Delete the Patron
            return true;  // Patron deleted successfully
        }
        return false; 
    }

    private Patron convertToEntity(PatronRequestDTO dto) {
        Patron Patron = new Patron();
        Patron.setName(dto.getName());
        Patron.setAddress(dto.getAddress());
        Patron.setEmail(dto.getEmail());
        Patron.setPhoneNumber(dto.getPhoneNumber());
        return Patron;
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
