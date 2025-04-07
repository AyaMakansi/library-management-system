package com.example.demo.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisteredEvent {

    private final String email;
    private final String username;
}
