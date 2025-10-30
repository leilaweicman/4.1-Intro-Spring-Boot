package cat.itacademy.s04.t01.userapi.repositories;

import cat.itacademy.s04.t01.userapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        repository.save(user);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findAll_returnsAllUsers() {

    }

    @Test
    void findById_returnsExistingUser() {

    }

    @Test
    void searchByName_returnsUsersMatchingName() {

    }

    @Test
    void existsByEmail_returnsTrueIfEmailExists() {

    }
}
