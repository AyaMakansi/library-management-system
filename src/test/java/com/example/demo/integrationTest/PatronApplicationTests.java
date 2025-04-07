package com.example.demo.integrationTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.demo.model.Patron;
import com.example.demo.repository.PatronRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PatronApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Patron> patrons = Arrays.asList(
            new Patron("patron1", "patron1@gmail.com", "09912312333", "Address1"),
            new Patron("patron2", "patron2@gmail.com", "09123147159", "Address2"),
            new Patron("patron3", "patron3@gmail.com", "09714853263", "Address3"));

    @BeforeEach
    void setup() {

        patronRepository.saveAll(patrons);
    }

    @AfterEach
    void clear() {
        patronRepository.deleteAll();
    }

    @Test
    public void GetPatronByIdTest() throws Exception {
        var id = patronRepository.findAll().getLast().getId();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/patrons/" + id);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(patrons.getLast().getName()))
                .andExpect(jsonPath("$.email").value(patrons.getLast().getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(patrons.getLast().getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(patrons.getLast().getAddress()));

    }

    @Test
    public void GetPatronByIdNotFoundTest() throws Exception {
        var id = UUID.randomUUID();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/patrons/" + id);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void GetAllPatronsTest() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/patrons");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    for (int i = 0; i < patrons.size(); i++) {
                        String patronPath = "$[" + i + "]";
                        Patron patron = patrons.get(i);
                        // Directly assert within the result
                        result.getResponse().getContentAsString(); // Optional: You can log or print the result if
                                                                   // needed
                        mockMvc.perform(requestBuilder) // Perform the request again if necessary
                                .andExpect(jsonPath(patronPath + ".name").value(patron.getName()))
                                .andExpect(jsonPath(patronPath + ".email").value(patron.getEmail()))
                                .andExpect(jsonPath(patronPath + ".phoneNumber").value(patron.getPhoneNumber()))
                                .andExpect(jsonPath(patronPath + ".address").value(patron.getAddress()));
                    }
                });

    }

    @Test
    public void ValidPatronCreation() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new Patron("createpatron", "creationPatron@gmail.com", "09213121563", "address4")));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

    }

    @Test
    public void inValidPatronCreation() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(new Patron("", "creationPatron@gmail.com", "09213121563", "address4")));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void ValidPatronUpdate() throws Exception {
        var id = patronRepository.findAll().getFirst().getId();
        // Create an updated Patron object
        Patron updatedPatron = new Patron("updatepatron", "updatePatron@gmail.com", "09213121563", "address5");

        // Create the request to update the patron
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/patrons/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPatron));

        // Perform the request and verify the response
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()) // Ensure status is OK
                .andExpect(jsonPath("$.name").value(updatedPatron.getName())) // Ensure the name is updated
                .andExpect(jsonPath("$.email").value(updatedPatron.getEmail())) // Ensure the email is updated
                .andExpect(jsonPath("$.phoneNumber").value(updatedPatron.getPhoneNumber())) // Ensure the phone numbe                                                                     // is updated
                .andExpect(jsonPath("$.address").value(updatedPatron.getAddress())); // Ensure the address is updated

        // Optionally, verify if the entity is updated in the database (after the
        // request)
        Patron patronInDb = patronRepository.findById(id).orElseThrow();
        assertEquals(updatedPatron.getName(), patronInDb.getName());
        assertEquals(updatedPatron.getEmail(), patronInDb.getEmail());
        assertEquals(updatedPatron.getPhoneNumber(), patronInDb.getPhoneNumber());
        assertEquals(updatedPatron.getAddress(), patronInDb.getAddress());

    }

    @Test
    public void ValidPatronDelete() throws Exception {
        // Get the first patron from the repository
        var patron = patronRepository.findAll().get(0);
        var id = patron.getId();
        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/" + id))
                .andExpect(status().isNoContent()); // Use .isOk() if your API returns 200 instead of 204
        // Verify that the patron is deleted from the database
        assertFalse(patronRepository.existsById(id));
    }
    @Test
    public void GetAllPatrons_EmptyList() throws Exception {
        // Ensure the database is empty before testing
        patronRepository.deleteAll();
        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());  // Expect 204 OK;
            assertEquals(patronRepository.findAll().size(), 0);
    }

}
