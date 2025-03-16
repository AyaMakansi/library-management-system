package com.example.demo.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patron")
@Builder
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
      private UUID Id;
      @Column(nullable = false)
      private String Name; 
      @Column(nullable = false)
      private String Email; 
      private String PhoneNumber;
      private String Address;
      @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<BorrowingRecord> borrowingRecords;
}
