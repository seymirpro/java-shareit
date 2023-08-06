package ru.practicum.shareit.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.user.DuplicateEmailException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    public void setSup() {
        Mockito.reset(userRepository);
        userService = new UserService(userRepository);
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = generator.nextObject(UserDto.class);
        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(UserMapper.toUser(userDto));
        UserDto userSaved = userService.createUser(userDto);
        userSaved.setId(userDto.getId());
        assertEquals(userDto.getId(), userSaved.getId());
        assertEquals(userDto.getName(), userSaved.getName());
        assertEquals(userDto.getEmail(), userSaved.getEmail());
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        User user1 = generator.nextObject(User.class);
        User user2 = generator.nextObject(User.class);
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll())
                .thenReturn(users);
        List<UserDto> userList = userService.getUsers();
        assertThat(userList)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    void shouldThrowExceptionIfUserWithAlreadyExistedEmailAdded() {

        when(userRepository.save(any(User.class)))
                .thenThrow(new DuplicateEmailException("Email already used"));
        User user = generator.nextObject(User.class);
        assertThrows(DuplicateEmailException.class, () -> userService.createUser(UserMapper
                .toUserDto(user)));
        Mockito.reset(userRepository);
        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    void testGetUserByID() {
        User user = generator.nextObject(User.class);
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userInDb = userService.getUserByID(user.getId());
        assertEquals(user.getId(), userInDb.getId());
        assertEquals(user.getName(), userInDb.getName());
        assertEquals(user.getEmail(), userInDb.getEmail());
    }

    void testUpdateUser() {

    }

    void testDeleteUserByID() {

    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(userRepository);
    }
}