package com.example.demo.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Examples.BorrowingRecordExample;
import com.example.demo.model.BorrowingRecord;
@Repository
public interface BorrowingRecordRepository extends  JpaRepository<BorrowingRecord,UUID>{

     default Optional<BorrowingRecord> findByBookIdAndPatronId(UUID bookId, UUID patronId) {
        Example<BorrowingRecord> example = BorrowingRecordExample.createExample(bookId, patronId);
        
        return findOne(example);  
    }

}
