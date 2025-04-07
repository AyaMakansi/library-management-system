package com.example.demo.service.Auth;

import com.example.demo.dto.Auth.LoginRequestDTO;
import com.example.demo.dto.Auth.LoginResponseDTO;
import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;
import com.example.demo.event.UserRegisteredEvent;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.filter.JwtUtil;
import com.example.demo.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper mapper;
 private final ApplicationEventPublisher publisher;

    /**
     * Registers a new user.
     * 
     * @param dto the user registration data transfer object
     * @return the response containing user details
     */
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {
        try {
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                log.error("Username already exists: {}", dto.getUsername());
                throw new UsernameNotFoundException(dto.getUsername());
            }

            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                log.error("Email already exists: {}", dto.getEmail());
                throw new EmailAlreadyExistsException(dto.getEmail());
            }

            // Create new user instance
            var user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword())); // Encode password
            user.setEmail(dto.getEmail());
            user.setRole(dto.getRole());
            // Save the user to the database
            userRepository.save(user);

            log.info("User registered successfully: {}", dto.getUsername());
            publisher.publishEvent(new UserRegisteredEvent(dto.getEmail(),dto.getUsername()));
            return mapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation: {}", e.getMessage());
            throw new RuntimeException("Database constraint violation: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while registering the user: " + e.getMessage());
        }
    }

    /**
     * Logs in a user by generating a JWT token.
     * 
     * @param loginRequest the login request data transfer object
     * @return a map containing the JWT token and user role
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        // Fetch user details by username
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (!userOptional.isPresent()) {
            throw new RuntimeException("Invalid username or password");
        }

        var user = userOptional.get();

        // Check if the provided password matches the stored password
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        var token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        var refrshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole());

        // Create a response map
        var response = new LoginResponseDTO(token, refrshToken);

        return response;
    }
}
