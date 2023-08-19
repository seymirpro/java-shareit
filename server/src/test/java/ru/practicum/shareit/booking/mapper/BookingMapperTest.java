package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDatesDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingMapperTest {

    @Test
    public void testFromBookingCreateUpdateDtoToBooking() {
        BookingCreateUpdateDto dto = BookingCreateUpdateDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .build();

        Booking booking = BookingMapper.fromBookingCreateUpdateDtoToBooking(dto);

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(dto.getStart(), booking.getStart());
        Assertions.assertEquals(dto.getEnd(), booking.getEnd());
    }

    @Test
    public void testFromBookingToBookingGetDto() {
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .booker(UserMapper.toUser(UserDto.builder().build().builder().id(2L).build()))
                .item(Item.builder().id(3L).build())
                .status(Status.WAITING)
                .build();

        BookingGetDto dto = BookingMapper.fromBookingToBookingGetDto(booking);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(booking.getId(), dto.getId());
        Assertions.assertEquals(booking.getStart(), dto.getStart());
        Assertions.assertEquals(booking.getEnd(), dto.getEnd());
        Assertions.assertEquals("WAITING", dto.getStatus());
        Assertions.assertEquals(0, dto.getBooker().getId());
        Assertions.assertEquals(3L, dto.getItem().getId());
    }

    @Test
    public void testFromBookingBookingWithoutDatesDto() {
        Booking booking = Booking.builder()
                .id(1)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(3L).build())
                .status(Status.WAITING)
                .build();

        BookingWithoutDatesDto dto = BookingMapper.fromBookingBookingWithoutDatesDto(booking);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(booking.getId(), dto.getId());
        Assertions.assertEquals(2L, dto.getBooker().getId());
        Assertions.assertEquals(3L, dto.getItem().getId());
        Assertions.assertEquals(Status.WAITING, dto.getStatus());
    }

    @Test
    void toBookingWithoutDatesDtoList_shouldMapBookingsToBookingWithoutDatesDtoList() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(Booking.builder()
                .id(1)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(3L).build())
                .status(Status.WAITING)
                .build());
        List<BookingWithoutDatesDto> bookingWithoutDatesDtoList = BookingMapper.toBookingWithoutDatesDtoList(bookings);
        Assertions.assertEquals(bookings.size(), bookingWithoutDatesDtoList.size());
    }
}