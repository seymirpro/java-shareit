package ru.practicum.shareit.booking.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemServiceImpl itemService;
    @InjectMocks
    private UserController userController;

    @SneakyThrows
    @Test
    void add() {
        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setItem(new Item());
        BookingCreateUpdateDto bookingDto = new BookingCreateUpdateDto();
        Long userId = 1L;
        when(bookingService.createBooking(userId, bookingDto))
                .thenReturn(BookingMapper.fromBookingToBookingGetDto(booking));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).createBooking(userId, bookingDto);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        long bookingId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookings() {
        long userId = 1L;
        mockMvc.perform(get("/bookings", userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookingsOwner() {
        long userId = 1L;
        mockMvc.perform(get("/bookings/owner", userId)).andExpect(status().isBadRequest());
    }
}