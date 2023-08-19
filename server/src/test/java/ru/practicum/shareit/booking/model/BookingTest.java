package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingTest {

    private Booking booking;

    @Mock
    private Item mockItem;

    @Mock
    private User mockBooker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .item(mockItem)
                .booker(mockBooker)
                .status(Status.WAITING)
                .build();
    }

    @Test
    public void testGetters() {
        Assertions.assertEquals(1, booking.getId());
        Assertions.assertNotNull(booking.getStart());
        Assertions.assertNotNull(booking.getEnd());
        Assertions.assertEquals(mockItem, booking.getItem());
        Assertions.assertEquals(mockBooker, booking.getBooker());
        Assertions.assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    public void testAllArgsConstructor() {
        Booking booking = new Booking(2, mockItem, mockBooker, Status.APPROVED);
        Assertions.assertEquals(2, booking.getId());
        Assertions.assertEquals(mockItem, booking.getItem());
        Assertions.assertEquals(mockBooker, booking.getBooker());
        Assertions.assertEquals(Status.APPROVED, booking.getStatus());
    }
}