package com.example.demo.dto.Auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
  
    private String username;
    private String password;
}
