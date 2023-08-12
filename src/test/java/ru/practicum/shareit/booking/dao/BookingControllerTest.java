package ru.practicum.shareit.booking.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
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

    private final String REQ_HEADER_USER_ID = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void add() {
        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setItem(new Item());
        BookingCreateUpdateDto bookingDto = new BookingCreateUpdateDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(3));
        bookingDto.setItemId(1L);
        Long userId = 1L;
        when(bookingService.createBooking(userId, bookingDto))
                .thenReturn(BookingMapper.fromBookingToBookingGetDto(booking));

        mockMvc.perform(post("/bookings")
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());

        verify(bookingService, never()).createBooking(userId, bookingDto);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        long bookingId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId))
                )
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllBookings() {
        Long userId = 1L;
        mockMvc.perform(get("/bookings")
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId))
                )
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllBookingsOwner() {
        long userId = 1L;
        mockMvc.perform(get("/bookings/owner", userId)
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId))
                )
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testUpdateBooking() {
        long bookingId = 1l;
        long userId = 1L;
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(REQ_HEADER_USER_ID, String.valueOf(userId)))
                .andExpect(status().isOk());
    }
}