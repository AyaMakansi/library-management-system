package com.example.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Auth.LoginRequestDTO;
import com.example.demo.dto.Auth.LoginResponseDTO;
import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;
import com.example.demo.service.Auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Auth")
@RequestMapping("/api/auth")
public class AuthController {

   private final AuthService authService;
  
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login", description = "Authenticate user and return access & refresh tokens")
    @ApiResponse(responseCode = "200", description = "Login successful", 
        content = @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @Operation(summary = "Register a new User", description = "Registers a new User or updates existing user data based on the provided request data")
    @ApiResponse(responseCode = "201", description = "Successfully registered User", 
        content = @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequest) {
       return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userRequest));
    }
   
}
