package com.example.demo.Listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.demo.event.UserRegisteredEvent;
import com.example.demo.service.user.EmailService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserEventListener {

     private final EmailService emailService;
     
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }
}
