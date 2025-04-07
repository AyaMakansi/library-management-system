package com.example.demo.service.mapper;

import org.springframework.stereotype.Component;
import com.example.demo.dto.User.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.security.filter.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final JwtUtil jwtUtil;
    public UserResponseDTO toDto(User user) {
        var dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setToken(jwtUtil.generateToken(user.getUsername(),user.getRole()));
        dto.setEmail(user.getEmail());
        return dto;
    }
}
