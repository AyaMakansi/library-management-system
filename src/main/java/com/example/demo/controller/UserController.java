package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;
import com.example.demo.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Tag(name = "User")
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Operation(summary = "Register a new User", description = "Registers a new User or updates existing user data based on the provided request data")
    @ApiResponse(responseCode = "201", description = "Successfully registered User", 
        content = @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.CreateUser(request));
    }


}
