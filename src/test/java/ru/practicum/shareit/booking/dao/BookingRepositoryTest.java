package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1).name("owner").email("owner@mail.ru").build();
        booker = User.builder().id(2).name("booker").email("booker@mail.ru").build();
        item = Item.builder().id(1).owner(owner).available(true).description("description").name("item").build();
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2023, 8, 1, 15, 32, 9))
                .end(LocalDateTime.of(2023, 8, 5, 15, 32, 9))
                .status(Status.APPROVED)
                .booker(booker)
                .item(item)
                .build();
        bookingRepository.save(booking);
    }

    @Test
    void findByBookerIdAndBookingId() {
        Booking booking = bookingRepository.findByBookerIdAndBookingId(2, 1);
        assertEquals(1, booking.getId());
    }

    @Test
    void findByItemOwnerIdAndBookingId() {
        Booking booking = bookingRepository.findByItemOwnerIdAndBookingId(1, 1);
        assertEquals(1, booking.getItem().getOwner().getId());
    }

    @Test
    void findAllByItemOwnerId() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerId(1, Pageable.ofSize(1));
        assertTrue(!bookings.isEmpty());
    }

    @Test
    void findFutureBookingsByItemOwnerId() {
        List<Booking> bookings = bookingRepository.findFutureBookingsByItemOwnerId(1, Pageable.ofSize(1));
        assertFalse(!bookings.isEmpty());
    }

    @Test
    void findAllByBookerId() {
        List<Booking> bookings = bookingRepository.findAllByBookerId(2, Pageable.ofSize(2));
        assertEquals(2, bookings.get(0).getBooker().getId());
        assertEquals(1, bookings.get(0).getId());
    }

    @Test
    void findCurrentBookingsByBookerId() {
        List<Booking> bookings = bookingRepository.findCurrentBookingsByBookerId(2L, Pageable.ofSize(2));
        assertEquals(0, bookings.size());
    }

    @Test
    void findPastBookingsByBookerId() {
        List<Booking> bookings = bookingRepository.findPastBookingsByBookerId(2L, Pageable.ofSize(2));
        assertEquals(2, bookings.get(0).getBooker().getId());
    }
}