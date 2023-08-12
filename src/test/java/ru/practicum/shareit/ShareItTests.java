package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ShareItTests {

    @Test
    void contextLoads() {
        assertThrows(IllegalStateException.class,
                () -> ShareItApp.main(new String[]{}));
    }

}
