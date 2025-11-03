package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.exceptions.EmailAlreadyExistsException;
import cat.itacademy.s04.t01.userapi.models.User;
import cat.itacademy.s04.t01.userapi.repositories.InMemoryUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
    }

    @BeforeEach
    void cleanUp() {
        repository.clear();
    }

    @Test
    void getUsers_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createUser_returnsUserWithId() throws Exception{
        User user = new User("Ada", "ada@example.com");
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Ada"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void getUserById_returnsCorrectUser() throws Exception{
        User newUser = new User("Grace", "grace@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User created = objectMapper.readValue(response, User.class);

        mockMvc.perform(get("/users/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId().toString()))
                .andExpect(jsonPath("$.name").value("Grace"))
                .andExpect(jsonPath("$.email").value("grace@example.com"));
    }

    @Test
    void getUserById_returnsNotFoundIfMissing() throws Exception{
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() throws Exception{
        User user1 = new User("John", "john@example.com");
        User user2 = new User("John", "john2@example.com");
        User user3 = new User("Alice Brown", "alice@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users?name=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(org.hamcrest.Matchers.containsStringIgnoringCase("john")));
    }

    @Test
    void createUser_returnsConflictIfEmailAlreadyExists() throws Exception {
        User user1 = new User("Ada Lovelace", "ada@example.com");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        User user2 = new User("Ada Duplicate", "ada@example.com");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(EmailAlreadyExistsException.class)
                                .hasMessageContaining("Email already exists"));

    }
}
