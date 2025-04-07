package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.dto.Book.BookRequestDTO;
import com.example.demo.dto.Book.BookResponseDTO;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")

public class BookControllerTest {

    @LocalServerPort
    private int port=8080;

    @Autowired
    private TestRestTemplate restTemplate;


    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/books";
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }



    /**
     * Test Create Book (POST)
     */
    @Test
    @Transactional
    public void testCreateBook() {
        var request = new BookRequestDTO();
        request.setTitle("Test Book");
        request.setAuthor("John Doe");
        request.setIsbn("1231231231231231");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<BookResponseDTO> response = restTemplate.postForEntity(getBaseUrl(), requestEntity,
                BookResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Book");
    }

    // /**
    // * Test Get All Books (GET)
    // */
    @Test
    public void testGetAllBooks() {

        var response = restTemplate.exchange(getBaseUrl(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BookResponseDTO>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * Test Get Book by ID (GET)
     */
    @Test
    public void testGetBookById() {
        // Use a known existing bookId or a non-existing one
        UUID bookId = UUID.fromString("b81267fe-1686-4b72-978d-a5a6af7e63a5");

        var response = restTemplate.exchange(getBaseUrl() + "/" + bookId, HttpMethod.GET, null,
                new ParameterizedTypeReference<BookResponseDTO>() {
                });

        // If the book exists, the response should be 200 OK and return the book details
        if (response.getStatusCode() == HttpStatus.OK) {
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(bookId);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // If the book is not found, assert that the status is 404 NOT FOUND
            assertThat(response.getBody()).isNull();
        }
    }

    // /**
    // * Test Update Book (PUT)
    // */
    @Test
    public void testUpdateBook() {
        UUID bookId = UUID.randomUUID();
        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Updated Book");
        request.setAuthor("Updated Author");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<BookResponseDTO> response = restTemplate.exchange(getBaseUrl() + "/" + bookId, HttpMethod.PUT,
                requestEntity, BookResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateBook1() {
        // Use a known existing bookId or a non-existing one for the test
        UUID bookId = UUID.fromString("your-predefined-uuid-here"); // Change to a valid or non-existing UUID

        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Updated Book");
        request.setAuthor("Updated Author");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<BookResponseDTO> response = restTemplate.exchange(getBaseUrl() + "/" + bookId, HttpMethod.PUT,
                requestEntity, BookResponseDTO.class);

        // Check if the book exists and assert the corresponding status code and
        // behavior
        if (response.getStatusCode() == HttpStatus.OK) {
            // If the book was successfully updated, assert the updated details
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("Updated Book");
            assertThat(response.getBody().getAuthor()).isEqualTo("Updated Author");
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // If the book was not found, assert the error response
            assertThat(response.getBody()).isNull();
        } else {
            // Handle unexpected response codes (optional)
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testDeleteBook() {
        // Use a known existing bookId
        UUID bookId = UUID.fromString("your-predefined-uuid-here");

        ResponseEntity<String> response = restTemplate.exchange(getBaseUrl() + "/" + bookId, HttpMethod.DELETE, null,
                String.class);

        // Assert 200 OK if the book is successfully deleted
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Book successfully deleted");
    }
}
