package com.example.demo.integrationTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.model.Book;
import com.example.demo.model.BorrowingRecord;
import com.example.demo.model.Patron;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowingRecordRepository;
import com.example.demo.repository.PatronRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BorrowingApplicationTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private BorrowingRecordRepository borrowingRecordRepository;

        @Autowired
        private BookRepository bookRepository;
        @Autowired
        private PatronRepository patronRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private List<Book> books = Arrays.asList(
                        new Book("Title1", "Author1", 2020, "1231231231234"),
                        new Book("Title2", "Author2", 2021, "1231231231236"),
                        new Book("Title3", "Author3", 2022, "1231231231238"));

        private List<Patron> patrons = Arrays.asList(
                        new Patron("patron1", "patron1@gmail.com", "09912312333", "Address1"),
                        new Patron("patron2", "patron2@gmail.com", "09123147159", "Address2"),
                        new Patron("patron3", "patron3@gmail.com", "09714853263", "Address3"));

        private List<BorrowingRecord> records;

        @BeforeEach
        void setup() {

                books = bookRepository.saveAll(books);
                patrons = patronRepository.saveAll(patrons);
                records = Arrays.asList(
                                new BorrowingRecord(),
                                new BorrowingRecord(),
                                new BorrowingRecord());
                                records.get(0).BorrowBook(books.get(0), patrons.get(0), LocalDate.now().minusDays(1));
                                records.get(1).BorrowBook(books.get(1), patrons.get(1), LocalDate.now().minusDays(1));
                                records.get(2).BorrowBook(books.get(2), patrons.get(2), LocalDate.now().minusDays(1));
                borrowingRecordRepository.saveAll(records);
        }

        @AfterEach
        void clear() {
                bookRepository.deleteAll();
                patronRepository.deleteAll();
                borrowingRecordRepository.deleteAll();
        }

        @Test
        public void ValidBorrowBook() throws Exception {

                var record=new BorrowingRecord();
                record.BorrowBook(books.get(0),
                patrons.get(0),
                LocalDate.parse("2025-03-24"));
                var requestBuilder = MockMvcRequestBuilders
                                .post("/api/borrow/" + books.get(0).getId() + "/patron/" + patrons.get(0).getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                                record));
                mockMvc.perform(requestBuilder)
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.bookId").value(books.get(0).getId().toString()))
                                .andExpect(jsonPath("$.patronId").value(patrons.get(0).getId().toString()))
                                .andExpect(jsonPath("$.borrowDate").value(LocalDate.parse("2025-03-26").toString()))                                                                                               
                                .andExpect(jsonPath("$.isAvailable").value(false));
        }

        @Test
        public void inValidBorrowBook() throws Exception {
                var record=new BorrowingRecord();
                record.BorrowBook(books.get(0),
                patrons.get(0),
                null);
                var requestBuilder = MockMvcRequestBuilders
                                .post("/api/borrow/" + books.get(0).getId() + "/patron/" + patrons.get(0).getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(record));
                mockMvc.perform(requestBuilder)
                                .andExpect(status().isBadRequest());

        }

        @Test
        public void ValidReturnBook() throws Exception {

                var record=new BorrowingRecord();
                record.setBook(records.get(0).getBook());
                record.setPatron( records.get(0).getPatron());
                record.setBorrowDate(records.get(0).getBorrowDate());
                record.ReturnBook(LocalDate.now());
               
                var requestBuilder = MockMvcRequestBuilders
                                .put("/api/return/" + records.get(0).getBookId() + "/patron/"
                                                + records.get(0).getPatronId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                            record));
                mockMvc.perform(requestBuilder)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.bookId").value(records.get(0).getBookId().toString()))
                                .andExpect(jsonPath("$.patronId").value(records.get(0).getPatronId().toString()))
                                .andExpect(jsonPath("$.returnDate").value(LocalDate.now().toString()))
                                .andExpect(jsonPath("$.isAvailable").value(true));
        }

        @Test
        public void inValidReturnBook() throws Exception {

                var record=new BorrowingRecord();
                record.setBook(records.get(0).getBook());
                record.setPatron( records.get(0).getPatron());
                record.setBorrowDate(records.get(0).getBorrowDate());
                record.ReturnBook(null);
                var requestBuilder = MockMvcRequestBuilders
                                .put("/api/return/" + records.get(0).getBookId() + "/patron/"
                                                + records.get(0).getPatronId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(record));
                mockMvc.perform(requestBuilder)
                                .andExpect(status().isBadRequest());

        }
}
