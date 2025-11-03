package cat.itacademy.s04.t01.userapi.services;

import cat.itacademy.s04.t01.userapi.exceptions.EmailAlreadyExistsException;
import cat.itacademy.s04.t01.userapi.models.User;
import cat.itacademy.s04.t01.userapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = new User("Ada Lovelace", "ada@example.com");

        when(userRepository.existsByEmail("ada@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_shouldSaveUserWhenEmailNotExists() {
        User user = new User("Ada", "ada@example.com");

        when(userRepository.existsByEmail("ada@example.com")).thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        verify(userRepository).save(user);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualToIgnoringCase("Ada");
        assertThat(result.getEmail()).isEqualToIgnoringCase("ada@example.com");
    }
}