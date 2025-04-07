package com.example.demo.service.user;

import org.springframework.stereotype.Service;

import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;

@Service
public interface UserService {

      public UserResponseDTO CreateUser(UserRequestDTO dto);
}
