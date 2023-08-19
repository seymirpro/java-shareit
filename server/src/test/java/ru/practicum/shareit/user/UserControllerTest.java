package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.user.DuplicateEmailException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final EasyRandom generator = new EasyRandom();

    @Test
    @DirtiesContext
    void testCreateUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@email.ru")
                .build();
        when(userService.createUser(any(UserDto.class)))
                .thenAnswer(invocationOnMock -> {
                    UserDto userDtoInput = invocationOnMock.getArgument(0, UserDto.class);
                    return userDtoInput;
                });

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @SneakyThrows
    void testGetUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        when(userService.getUsers()).thenReturn(userDtos);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldThatShouldNotExist").doesNotExist());
    }

    @Test
    @SneakyThrows
    void testGetUserByID() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@email.ru")
                .build();
        when(userService.getUserByID(anyLong()))
                .thenReturn(userDto);
        long id = 1L;
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldReturnExceptionIfUserDoesNotExist() {
        UserDto user = UserDto.builder()
                .email("user@mail.ru")
                .build();
        String jsonUser = objectMapper.writeValueAsString(user);
        when(userService.updateUser(1L, user))
                .thenThrow(new UserDoesNotExistException());

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(anyLong(), any(UserDto.class));
    }

    @Test
    @SneakyThrows
    void shouldReturnExceptionIfUserEmailAlreadyUsed() {
        UserDto userWithDuplicateEmail = UserDto.builder()
                .name("user")
                .email("duplicate@mail.ru")
                .build();
        String jsonUser = objectMapper.writeValueAsString(userWithDuplicateEmail);
        when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(new DuplicateEmailException("email already used"));

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void shouldGetExceptionWithCreateUserWithoutName() throws Exception {
        UserDto userWithoutName = UserDto.builder()
                .name(null)
                .email("user@mail.ru")
                .build();
        String jsonUser = objectMapper.writeValueAsString(userWithoutName);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    @SneakyThrows
    void shouldUpdateUserWithoutName() throws Exception {
        UserDto userWithoutName = UserDto.builder()
                .id(1L)
                .email("user@mail")
                .build();
        when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(userWithoutName);

        String jsonUser = objectMapper.writeValueAsString(userWithoutName);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userWithoutName.getId()))
                .andExpect(jsonPath("$.name").value(userWithoutName.getName()))
                .andExpect(jsonPath("$.email").value(userWithoutName.getEmail()));
        verify(userService, times(1)).updateUser(anyLong(),
                any(UserDto.class));
    }

    @Test
    @SneakyThrows
    void testDeleteUserByID() {
        when(userService.deleteUserByID(anyLong()))
                .thenReturn(any(UserDto.class));
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}