package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingOwnerGetDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.Collection;
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

    public ItemOwnerDto(long id, String name, String description,
                        boolean available, Booking lastBooking, Booking nextBooking,
                        Collection<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = BookingMapper.fromBookingToBookingGetOwnerDto(lastBooking);
        this.nextBooking = BookingMapper.fromBookingToBookingGetOwnerDto(nextBooking);
        this.comments = CommentMapper.toCommentGetDtos(comments);
    }


    public ItemOwnerDto(long id, String name, String description, Boolean available, Collection<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.comments = CommentMapper.toCommentGetDtos(comments);
    }
}