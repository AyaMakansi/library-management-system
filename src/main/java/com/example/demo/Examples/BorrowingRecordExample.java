package com.example.demo.Examples;

import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.example.demo.model.BorrowingRecord;

public class BorrowingRecordExample {
 public static Example<BorrowingRecord> createExample(UUID bookId, UUID patronId) {
        BorrowingRecord exampleBorrowingRecord = new BorrowingRecord();
        exampleBorrowingRecord.setBookById(bookId);
        exampleBorrowingRecord.setPatronById(patronId);

        // Create an example matcher to ignore null fields and make the query more flexible
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()  // Ignores fields that are null
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);  // Allows for partial matching if necessary

        return Example.of(exampleBorrowingRecord, matcher);
    }
}
