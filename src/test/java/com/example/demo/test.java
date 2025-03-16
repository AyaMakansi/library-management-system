// package com.example.demo;
// import com.example.demo.dto.Book.BookRequestDTO;
// import com.example.demo.model.Book;
// import com.example.demo.repository.BookRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.hamcrest.Matchers.is;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class test {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Autowired
//     private BookRepository bookRepository; // To verify database interactions

//     @Test
//     public void testCreateBook() throws Exception {
//         // Create a request DTO
//         BookRequestDTO request = new BookRequestDTO();
//         request.setTitle("Test Book");
//         request.setAuthor("John Doe");
//         request.setIsbn("1231231231231231");

//         // Perform the POST request
//         mockMvc.perform(post("/books")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated()) // Expect HTTP 201 Created
//                 .andExpect(jsonPath("$.title", is("Test Book"))) // Check response body
//                 .andExpect(jsonPath("$.author", is("John Doe")))
//                 .andExpect(jsonPath("$.isbn", is("1231231231231231")));

//         // Verify that the data is saved in the database
//         Book savedBook = bookRepository.findById("1231231231231231").orElseThrow();
//         assertThat(savedBook.getTitle()).isEqualTo("Test Book");
//         assertThat(savedBook.getAuthor()).isEqualTo("John Doe");
//     }
// }
