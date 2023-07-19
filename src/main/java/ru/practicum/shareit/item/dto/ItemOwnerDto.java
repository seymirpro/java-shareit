package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingOwnerGetDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOwnerDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingOwnerGetDto lastBooking;
    private BookingOwnerGetDto nextBooking;
    private List<CommentGetDto> comments;
}
