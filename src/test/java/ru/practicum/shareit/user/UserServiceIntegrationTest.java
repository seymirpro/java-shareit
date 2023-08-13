package ru.practicum.shareit.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    private final EasyRandom generator = new EasyRandom();

    @Test
    @DirtiesContext
    void testCreateUser() {
        UserDto userDto = generator.nextObject(UserDto.class);
        userDto.setId(1000L);
        userDto.setEmail("asdcdc@mail.ru");
        UserDto createdUserDto = userService.createUser(userDto);
        assertNotNull(createdUserDto.getId());
        assertNotNull(createdUserDto);
    }
}