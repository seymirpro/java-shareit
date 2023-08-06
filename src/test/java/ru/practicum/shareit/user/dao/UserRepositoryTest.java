package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1).email("user@mail.ru").name("user").build();
        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        Optional<User> userOptional = userRepository.findByEmail("user@mail.ru");
        User user = userOptional.get();
        assertEquals(1, user.getId());
        assertEquals("user@mail.ru", user.getEmail());
        assertEquals("user", user.getName());
    }
}