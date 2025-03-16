package com.example.demo.dto.Patron;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatronResponseDTO {

      private UUID Id;
      private String Name; 
      
      private String Email;
       
      private String PhoneNumber;
      
      private String Address;
}
