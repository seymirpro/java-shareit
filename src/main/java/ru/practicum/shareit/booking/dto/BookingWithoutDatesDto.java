package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
public class BookingWithoutDatesDto {
    private long id;
    private UserDto booker;
    private ItemDto item;
    private Status status;
}
