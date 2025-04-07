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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.filter.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class BookApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private User adminUser = new User("admin", "AdminPassword@1234", "Admin", "admin@gmail.com");
    private List<Book> books = Arrays.asList(
            new Book("Title1", "Author1", 2020, "1231231231234"),
            new Book("Title2", "Author2", 2021, "1231231231236"),
            new Book("Title3", "Author3", 2022, "1231231231238"));

    @BeforeEach
    void setup() {
        userRepository.save(adminUser);
        bookRepository.saveAll(books);
    }

    @AfterEach
    void clear() {
        userRepository.delete(adminUser);
        bookRepository.deleteAll();
    }

    // Use a helper method to generate tokens
    private String generateValidToken(String username) throws Exception {
        String loginPayload = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, "TestPassword@123");

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        var jsonNode = objectMapper.readTree(responseBody);
        return "Bearer " + jsonNode.get("token").asText();
    }

    @Test
    public void testGetBookById() throws Exception {
     // 3️⃣ Fetch Book by ID
        var bookId = bookRepository.findAll().getLast().getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.author").isNotEmpty())
                .andExpect(jsonPath("$.isbn").isNotEmpty())
                .andExpect(jsonPath("$.publicationYear").isNotEmpty());
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    for (int i = 0; i < books.size(); i++) {
                        String bookPath = "$[" + i + "]";
                        Book book = books.get(i);
                        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath(bookPath + ".title").value(book.getTitle()))
                                .andExpect(jsonPath(bookPath + ".author").value(book.getAuthor()))
                                .andExpect(jsonPath(bookPath + ".isbn").value(book.getIsbn()))
                                .andExpect(jsonPath(bookPath + ".publicationYear").value(book.getPublicationYear()));
                    }
                });
    }

    // Ensure Book Creation logic is separate and uses helper methods
    @Test
    public void testValidBookCreation() throws Exception {
        Book newBook = new Book("Title4", "Author4", 2022, "1231231231239");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testValidBookUpdate() throws Exception {
        var id = bookRepository.findAll().getFirst().getId();
        Book updatedBook = new Book("Title6", "Author6", 2022, "1231231231230");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(updatedBook.getIsbn()))
                .andExpect(jsonPath("$.publicationYear").value(updatedBook.getPublicationYear()));

        var bookInDb = bookRepository.findById(id).orElseThrow();
        assertEquals(updatedBook.getTitle(), bookInDb.getTitle());
        assertEquals(updatedBook.getAuthor(), bookInDb.getAuthor());
        assertEquals(updatedBook.getIsbn(), bookInDb.getIsbn());
        assertEquals(updatedBook.getPublicationYear(), bookInDb.getPublicationYear());
    }
}
