package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.exception.user.DuplicateEmailException;
import ru.practicum.shareit.exception.user.UserAlreadyExistsException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private UserController userController;

    @Mock
    private ItemController itemController;

    @Mock
    private BookingController bookingController;

    @Mock
    private ItemRequestController itemRequestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController, itemController, bookingController, itemRequestController)
                .setControllerAdvice(new ErrorHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testHandleAlreadyExistsException() throws Exception {
        when(userController.getUserByID(anyLong()))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(get("/users/{id}", 9))
                .andExpect(status().isConflict());
    }

    @Test
    public void testHandleValidationException() throws Exception {
        when(userController.createUser(any(UserDto.class)))
                .thenThrow(new DuplicateEmailException("Email already exists"));

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"test@example.com\", \"name\":\"user\"}")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testHandleUserDoesNotExistException() throws Exception {
        when(userController.getUserByID(anyLong()))
                .thenThrow(new UserDoesNotExistException());

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleGeneralExceptions() throws Exception {
        when(userController
                .getUsers()).
                thenThrow(
                        new IllegalArgumentException("Invalid argument"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isInternalServerError());
    }
}
