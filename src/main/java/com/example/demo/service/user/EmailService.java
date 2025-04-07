package com.example.demo.service.user;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    public void sendWelcomeEmail(String toEmail, String username);
}
