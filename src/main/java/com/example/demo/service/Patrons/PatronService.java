package com.example.demo.service.Patrons;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;
import com.example.demo.exception.PatronExceptions.PatronNotFoundException;

@Service
public interface PatronService {

     public List<PatronResponseDTO> getAllPatrons();
     public PatronResponseDTO getPatronById(UUID id) throws PatronNotFoundException;
     public PatronResponseDTO createPatron(PatronRequestDTO patronRequestDTO);
     public PatronResponseDTO updatePatron(UUID id, PatronRequestDTO patronRequestDTO);
     public void deletePatron(UUID id);
}
