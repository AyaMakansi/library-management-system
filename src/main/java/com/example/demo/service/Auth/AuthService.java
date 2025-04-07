package com.example.demo.service.Auth;


import org.springframework.stereotype.Service;

import com.example.demo.dto.Auth.LoginRequestDTO;
import com.example.demo.dto.Auth.LoginResponseDTO;
import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;

@Service
public interface AuthService {

    public LoginResponseDTO login(LoginRequestDTO loginRequest);

    public UserResponseDTO registerUser(UserRequestDTO dto);
}
