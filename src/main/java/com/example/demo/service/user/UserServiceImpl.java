package com.example.demo.service.user;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.User.UserRequestDTO;
import com.example.demo.dto.User.UserResponseDTO;
import com.example.demo.event.UserRegisteredEvent;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.mapper.UserMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;
 private final ApplicationEventPublisher publisher;
    

    @Transactional
    public UserResponseDTO CreateUser(UserRequestDTO dto)
    {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
         // Create a User instance using the builder pattern
         User user =new User();
         user.setUsername(dto.getUsername());
         user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
         user.setRole(dto.getRole());
         userRepository.save(user);
         var res=mapper.toDto(user);
         publisher.publishEvent(new UserRegisteredEvent(dto.getEmail(),dto.getUsername()));
        return res;
    }


}
