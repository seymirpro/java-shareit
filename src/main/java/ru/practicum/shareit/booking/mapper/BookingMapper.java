package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingOwnerGetDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDatesDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class BookingMapper {
    public Booking fromBookingCreateUpdateDtoToBooking(BookingCreateUpdateDto bookingCreateUpdateDto) {
        Booking booking = Booking.builder()
                .start(bookingCreateUpdateDto.getStart())
                .end(bookingCreateUpdateDto.getEnd())
                .build();
        return booking;
    }

    public BookingGetDto fromBookingToBookingGetDto(Booking booking) {
        BookingGetDto bookingGetDto = BookingGetDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.toUserDtoGetId(booking.getBooker()))
                .item(ItemMapper.toItemDto(booking.getItem()))
                .status(String.valueOf(booking.getStatus()))
                .build();
        return bookingGetDto;
    }

    public BookingWithoutDatesDto fromBookingBookingWithoutDatesDto(Booking booking) {
        return BookingWithoutDatesDto.builder()
                .id(booking.getId())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .item(ItemMapper.toItemDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public List<BookingGetDto> toBookingGetDtoList(List<Booking> bookings) {
        return bookings.stream().map(e -> fromBookingToBookingGetDto(e))
                .collect(Collectors.toList());
    }

    public List<BookingWithoutDatesDto> toBookingWithoutDatesDtoList(List<Booking> bookings) {
        return bookings.stream().map(e -> fromBookingBookingWithoutDatesDto(e))
                .collect(Collectors.toList());
    }

    public static BookingOwnerGetDto fromBookingToBookingGetOwnerDto(Booking booking) {
        return BookingOwnerGetDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
