package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Patron;

@Repository
public interface PatronRepository extends JpaRepository<Patron,UUID>{

}
