package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(100).email("user100@mail.ru").name("user").build();
        userRepository.save(user);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findByEmail() {
        Optional<User> userOptional = userRepository.findByEmail("user100@mail.ru");
        User user = userOptional.get();
        assertEquals(1, user.getId());
        assertEquals("user100@mail.ru", user.getEmail());
        assertEquals("user", user.getName());
    }
}