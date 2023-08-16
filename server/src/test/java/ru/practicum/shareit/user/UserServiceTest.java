package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.user.DuplicateEmailException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void testGetUserByID() {
        User user = generator.nextObject(User.class);
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userInDb = userService.getUserByID(user.getId());
        assertEquals(user.getId(), userInDb.getId());
        assertEquals(user.getName(), userInDb.getName());
        assertEquals(user.getEmail(), userInDb.getEmail());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void shouldReturnExceptionWhenCallingGetUserByIdWhichDoesNotExist() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class, () -> userService.getUserByID(100L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void testUpdateUser() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.ru")
                .build();
        User userSaved = userRepository.save(UserMapper.toUser(userDto));
        UserDto userUpdateDto = UserDto.builder()
                .id(1L)
                .name("userUPD")
                .email("userUPD@mail.ru")
                .build();
        when(userRepository.save(any(User.class)))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class, () -> userService.updateUser(1L, userUpdateDto));
    }

    @Test
    void shouldReturnExceptionWhenUpdatingUserThatDoesNotExist() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserDoesNotExistException());
        UserDto userDto = generator.nextObject(UserDto.class);
        assertThrows(UserDoesNotExistException.class, () -> userService.updateUser(userDto.getId(), userDto));
    }

    @Test
    @SneakyThrows
    void testUpdateUserByIDWithDuplicateEmail() {
        long userId = 1L;
        UserDto userDto = UserDto.builder()
                .name("usr update")
                .email("user@mail.ru")
                .build();
        User user = User.builder().id(1).name("user").email("user@mail.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(
                invocationOnMock -> {
                    User userToUpdate = invocationOnMock.getArgument(0);
                    userToUpdate.setEmail("user@mail.ru");
                    userToUpdate.setName(userDto.getName());
                    userToUpdate.setId(userId);
                    return userToUpdate;
                }
        );
        UserDto updatedUser = userService.updateUser(userId, userDto);
        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeleteNonExistingUser() {
        long userId = 1L;
        User user = User.builder()
                .id(100L)
                .name("user")
                .email("user@mail.ru")
                .build();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(anyLong());
        userService.deleteUserByID(userId);
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(userRepository);
    }
}