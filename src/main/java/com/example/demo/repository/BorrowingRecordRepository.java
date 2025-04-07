package com.example.demo.repository;


import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BorrowingRecord;

@Repository
public interface BorrowingRecordRepository extends  JpaRepository<BorrowingRecord,UUID>{

    Optional<BorrowingRecord> findByBook_IdAndPatron_Id(UUID bookId, UUID patronId);
}
