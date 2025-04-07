package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.BorrowingRecord.ReturnRequestDTO;
import com.example.demo.dto.BorrowingRecord.BorrowingRecordResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BorrowingRecordControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBorrowBook() {
        // Prepare mock data for the request
        UUID bookId = UUID.randomUUID();
        UUID patronId = UUID.randomUUID();
        
        ReturnRequestDTO request = new ReturnRequestDTO();
        //request.setBorrowDate(LocalDate.now());
        request.setReturnDate(LocalDate.now().plusWeeks(2));  // Example return date
    
        // Set up mock service behavior
        BorrowingRecordResponseDTO expectedResponse = new BorrowingRecordResponseDTO();
        expectedResponse.setBookId(bookId);
        expectedResponse.setPatronId(patronId);
      //  expectedResponse.setBorrowDate(request.getBorrowDate());
        expectedResponse.setReturnDate(request.getReturnDate());
    
        // Perform the POST request using TestRestTemplate
        ResponseEntity<BorrowingRecordResponseDTO> response = restTemplate.exchange(
                getBaseUrl() + "/borrow/" + bookId + "/patron/" + patronId,
                HttpMethod.POST,
                new HttpEntity<>(request),
                BorrowingRecordResponseDTO.class
        );
    
        // Assert the response status and content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBookId()).isEqualTo(bookId);
        assertThat(response.getBody().getPatronId()).isEqualTo(patronId);
       // assertThat(response.getBody().getBorrowDate()).isEqualTo(request.getBorrowDate());
        assertThat(response.getBody().getReturnDate()).isEqualTo(request.getReturnDate());
    }
    
    @Test
    public void testReturnBook() {
        UUID bookId = UUID.randomUUID();
        UUID patronId = UUID.randomUUID();
        
        // Prepare the mock request for return
        ReturnRequestDTO request = new ReturnRequestDTO();
        request.setReturnDate(LocalDate.now());
    
        // Prepare the expected response for a successful return
        BorrowingRecordResponseDTO expectedResponse = new BorrowingRecordResponseDTO();
        expectedResponse.setBookId(bookId);
        expectedResponse.setPatronId(patronId);
        expectedResponse.setReturnDate(request.getReturnDate());
    
        // Prepare the HttpEntity with request body and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReturnRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // Perform the PUT request using TestRestTemplate (HTTP exchange)
        ResponseEntity<BorrowingRecordResponseDTO> response = restTemplate.exchange(
                getBaseUrl() + "/return/{bookId}/patron/{patronId}", 
                HttpMethod.PUT, 
                requestEntity, 
                BorrowingRecordResponseDTO.class, 
                bookId, patronId
        );
    
        // Assert the response status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Assert that the returned body matches the expected values
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBookId()).isEqualTo(bookId);
        assertThat(response.getBody().getPatronId()).isEqualTo(patronId);
        assertThat(response.getBody().getReturnDate()).isEqualTo(request.getReturnDate());
    }
    
   
    

}
