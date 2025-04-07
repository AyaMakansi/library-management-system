package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "patron")
@NoArgsConstructor
public class Patron extends BaseEntity{

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "phoneNumber", nullable = true)
  private String phoneNumber;

  @Column(name = "address", nullable = true)
  private String address;

  @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BorrowingRecord> borrowingRecords;

  public Patron(String name,String email,String phoneNumber,String address) {
    super();
    this.name=name;
    this.email=email;
    this.phoneNumber=phoneNumber;
    this.address=address;
  }

}
