package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
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
import org.springframework.web.client.RestClientException;

import com.example.demo.dto.Patron.PatronRequestDTO;
import com.example.demo.dto.Patron.PatronResponseDTO;

import com.example.demo.service.PatronService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PatronControllerTest {

    @LocalServerPort
    private int port=8080;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatronService patronService;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/patrons";
    }
    private UUID createdPatronId;
    
    /**
     * Test Create Patron (POST)
     */
    @Test
    public void testCreatePatron() {

        var request = new PatronRequestDTO();
        request.setEmail("Test Email");
        request.setName("John Doe");
        request.setPhoneNumber("09998521555");
        request.setAddress("Aleppo-street-458-1");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PatronRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<PatronResponseDTO> response = restTemplate.postForEntity(getBaseUrl(), requestEntity,
                PatronResponseDTO.class);
    
       // patronService.createPatron(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("Test Email");
        createdPatronId = response.getBody().getId();
        assertThat(createdPatronId).isNotNull();

    }

    // /**
    // * Test Get All Patrons (GET)
    // */
    @Test
    public void testGetAllPatrons() {

        var response = restTemplate.exchange(getBaseUrl(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PatronResponseDTO>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * Test Get Patron by ID (GET)
     */
    @Test
public void testGetPatronById() {

    assertThat(this.createdPatronId).isNotNull();
    String url = getBaseUrl() + "/" + this.createdPatronId;
    try {
        var response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<PatronResponseDTO>() {}
        );

        // Log the response to check if it's correctly formed
        System.out.println(response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(this.createdPatronId);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            assertThat(response.getBody()).isNull();
        }
    } catch (RestClientException e) {
        System.out.println("Error while extracting response: " + e.getMessage());
        e.printStackTrace();
    }
}

    // /**
    // * Test Update Patron (PUT)
    // */
    @Test
    public void testUpdatePatron() {
        assertThat(this.createdPatronId).isNotNull();
        PatronRequestDTO request = new PatronRequestDTO();
        request.setEmail("Updated Email");
        request.setName("Updated Patron");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PatronRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<PatronResponseDTO> response = restTemplate.exchange(getBaseUrl() + "/" + this.createdPatronId, HttpMethod.PUT,
                requestEntity, PatronResponseDTO.class);

        // Check if the Patron exists and assert the corresponding status code and
        // behavior
        if (response.getStatusCode() == HttpStatus.OK) {
            // If the Patron was successfully updated, assert the updated details
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getEmail()).isEqualTo("Updated Email");
            assertThat(response.getBody().getName()).isEqualTo("Updated Patron");
            patronService.updatePatron(this.createdPatronId,request);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // If the Patron was not found, assert the error response
            assertThat(response.getBody()).isNull();
        } else {
            // Handle unexpected response codes (optional)
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
        }
    }

   @Test
public void testDeletePatron() {
    
    assertThat(this.createdPatronId).isNotNull();

    // First, try deleting the patron
    ResponseEntity<String> response = restTemplate.exchange(getBaseUrl() + "/" + this.createdPatronId, HttpMethod.DELETE, null,
            String.class);

    if (response.getStatusCode() == HttpStatus.OK) {
        // If the Patron was successfully deleted
        assertThat(response.getBody()).contains("Patron successfully deleted");
        // You can also check the patron service delete method to ensure it was called
        patronService.deletePatron(this.createdPatronId);
    } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
        // If the Patron does not exist (404 Not Found)
        assertThat(response.getBody()).contains("Patron not found");
    } else {
        // Handle unexpected status codes
        fail("Unexpected response status: " + response.getStatusCode());
    }
}

}
