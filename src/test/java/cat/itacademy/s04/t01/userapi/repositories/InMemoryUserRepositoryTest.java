package cat.itacademy.s04.t01.userapi.repositories;

import cat.itacademy.s04.t01.userapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void save_returnsAddedUser() {
        User user = new User("John", "john@example.com");
        User savedUser = repository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
    }

    @Test
    void findAll_returnsAllUsers() {
        repository.save(new User("Ada", "ada@example.com"));
        repository.save(new User("Alan", "alan@example.com"));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findById_returnsExistingUser() {
        User user = repository.save(new User("Ada", "ada@example.com"));

        assertTrue(repository.findById(user.getId()).isPresent());
        assertEquals("Ada", repository.findById(user.getId()).get().getName());
    }

    @Test
    void searchByName_returnsUsersMatchingName() {
        repository.save(new User("Ada", "ada@example.com"));
        repository.save(new User("Alan", "alan@example.com"));
        repository.save(new User("Grace", "grace@example.com"));

        List<User> users = repository.searchByName("Ada");

        assertEquals(1, users.size());
        assertEquals("Ada Lovelace", users.get(0).getName());
    }

    @Test
    void existsByEmail_returnsTrueIfEmailExists() {

    }
}
