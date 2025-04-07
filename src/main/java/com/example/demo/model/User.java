package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class User extends  BaseEntity{

    @NotBlank(message = "username is required")
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    @Column(nullable = false)
    private String role; // ✅ Role field added

    @Email
    @NotNull
    private String email;

    public User(String username, String password, String role,String email) {
        super(); // Calls BaseEntity default constructor
        this.username = username;
        this.password = password;
        this.role = role;
        this.email=email;
    }

}
