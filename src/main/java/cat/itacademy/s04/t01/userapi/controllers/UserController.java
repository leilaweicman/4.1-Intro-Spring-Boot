package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.exceptions.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private static final List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID());
        users.add(user);
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

}
