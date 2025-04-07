package com.example.demo.service.Patrons;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;
import com.example.demo.exception.PatronExceptions.PatronNotFoundException;
import com.example.demo.model.Patron;
import com.example.demo.repository.PatronRepository;
import com.example.demo.service.mapper.PatronMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PatronServiceImpl implements PatronService {
    private final PatronMapper mapper;
    private PatronRepository repository;


    public List<PatronResponseDTO> getAllPatrons() {
        log.info("Fetching all patrons");
        var start = System.currentTimeMillis();

        var patrons = repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        var elapsedTime = System.currentTimeMillis() - start;
        log.info("Fetched {} patrons in {} ms", patrons.size(), elapsedTime);

        return patrons;
    }

    @Cacheable(value = "patrons", key = "#id") // Cache patron by ID
    public PatronResponseDTO getPatronById(UUID id) throws PatronNotFoundException {
        log.info("Fetching patron with ID: {}", id);
        var patron = findPatronOrThrow(id);
        return mapper.toDTO(patron);
    }

    @Transactional
    public PatronResponseDTO createPatron(PatronRequestDTO patronRequestDTO) {
        log.info("Creating a new patron: {}", patronRequestDTO.getName());
        var patron = mapper.toEntity(patronRequestDTO);

        var start = System.currentTimeMillis();
        var savedPatron = repository.save(patron);
        var elapsedTime = System.currentTimeMillis() - start;

        log.info("Patron created successfully with ID: {} in {} ms", savedPatron.getId(), elapsedTime);
        return mapper.toDTO(savedPatron);
    }

    @Transactional
    public PatronResponseDTO updatePatron(UUID id, PatronRequestDTO patronRequestDTO) {
        log.info("Updating patron with ID: {}", id);
        var patron =findPatronOrThrow(id);
        patron.setName(patronRequestDTO.getName());
        patron.setAddress(patronRequestDTO.getAddress());
        patron.setEmail(patronRequestDTO.getEmail());
        patron.setPhoneNumber(patronRequestDTO.getPhoneNumber());

        var start = System.currentTimeMillis();
        var updatedPatron = repository.save(patron);
        var elapsedTime = System.currentTimeMillis() - start;

        log.info("Patron updated successfully with ID: {} in {} ms", id, elapsedTime);
        return mapper.toDTO(updatedPatron);
    }

    @Transactional
    public void deletePatron(UUID id) {
        log.info("Attempting to delete patron with ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Delete failed - Patron not found with ID: {}", id);
            throw new PatronNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Patron with ID: {} deleted successfully", id);
    }
    private Patron findPatronOrThrow(UUID id) {
        return repository.findById(id)
        .orElseThrow(() -> {
            log.error("Patron not found with ID: {}", id);
            return new PatronNotFoundException(id);  // A custom exception indicating a not-found book
        });
    }
    

}
