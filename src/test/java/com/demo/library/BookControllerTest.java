package com.demo.library;

import com.demo.library.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BookController using MockMvc.
 *
 * @SpringBootTest — loads the full application context.
 * @AutoConfigureMockMvc — sets up MockMvc automatically.
 *
 * MockMvc lets us test the HTTP layer without starting a real server.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("GET /api/books → returns seeded list of books")
    void getAllBooks_returnsSeededData() throws Exception {
        mockMvc.perform(get("/api/books"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
               .andExpect(jsonPath("$[0].title", notNullValue()));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/books → creates a new book, returns 201 Created")
    void createBook_returns201WithLocation() throws Exception {
        Book newBook = new Book("Spring Boot in Action", "Craig Walls", 2016,
                "A practical guide to Spring Boot application development.");

        mockMvc.perform(post("/api/books")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(newBook)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andExpect(jsonPath("$.id", notNullValue()))
               .andExpect(jsonPath("$.title", is("Spring Boot in Action")));
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/books with blank title → returns 400 Bad Request")
    void createBook_withBlankTitle_returns400() throws Exception {
        Book invalid = new Book("", "Some Author", 2020, "Description");

        mockMvc.perform(post("/api/books")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.fields.title", notNullValue()));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/books/{id} with unknown ID → returns 404")
    void getBookById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/books/99999"))
               .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @DisplayName("GET /actuator/health → returns UP status")
    void actuatorHealth_returnsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status", is("UP")));
    }
}
