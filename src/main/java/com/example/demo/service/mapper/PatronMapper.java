package com.example.demo.service.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;
import com.example.demo.model.Patron;

@Component
public class PatronMapper {
  public Patron toEntity(PatronRequestDTO dto) {
        var patron = new Patron();
        patron.setName(dto.getName());
        patron.setAddress(dto.getAddress());
        patron.setEmail(dto.getEmail());
        patron.setPhoneNumber(dto.getPhoneNumber());
        return patron;
    }

    public PatronResponseDTO toDTO(Patron patron) {
        var dto = new PatronResponseDTO();
        dto.setId(patron.getId());
        dto.setEmail(patron.getEmail());
        dto.setAddress(patron.getAddress());
        dto.setPhoneNumber(patron.getPhoneNumber());
        dto.setName(patron.getName());
        return dto;
    }
}
