package ru.practicum.shareit.user.mapper;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoGetId;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapperTest {
    private final EasyRandom generator = new EasyRandom();

    @Test
    void toUserDto() {
        User user = generator.nextObject(User.class);
        UserDto userDto = UserMapper.toUserDto(user);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUsersDto() {
        List<User> users = new ArrayList<>();
        User user1 = generator.nextObject(User.class);
        User user2 = generator.nextObject(User.class);
        users.add(user1);
        users.add(user2);
        List<UserDto> userDtos = UserMapper.toUsersDto(users);
        assertEquals(users.get(0).getId(), userDtos.get(0).getId());
        assertEquals(users.get(1).getId(), userDtos.get(1).getId());
    }

    @Test
    void toUserDtoGetId() {
        User user = generator.nextObject(User.class);
        UserDtoGetId userDtoGetId = UserMapper.toUserDtoGetId(user);
        assertEquals(user.getId(), userDtoGetId.getId());
    }
}