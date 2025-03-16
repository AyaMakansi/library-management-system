package com.example.demo.dto.Patron;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatronRequestDTO {

      @NotNull(message = "Name cannot be null")
      @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
      private String name;
  
      @Email(message = "Email must be valid")
      @NotNull(message = "Email cannot be null")
      private String email;
  
      @Nullable
      private String phoneNumber;
  
      @Nullable
      private String address;
}
