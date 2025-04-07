package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.BookController;
import com.example.demo.controller.BorrowingRecordController;
import com.example.demo.controller.PatronController;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    

    @Autowired
    private BookController bookController;


    @Autowired
    private PatronController patronController ;

    @Autowired
    private BorrowingRecordController borrowingController;

    @Autowired 
    private MockMvc mockMvc;

    
    @Test
    void  BookControllerTest()
    {
      assertNotNull(bookController);
      assertNotNull(mockMvc);
    }
    @Test
    void  PatronControllerTest()
    {
      assertNotNull(patronController);
      assertNotNull(mockMvc);
    }
    @Test
    void  BorrowingControllerTest()
    {
      assertNotNull(borrowingController);
      assertNotNull(mockMvc);
    }
}
