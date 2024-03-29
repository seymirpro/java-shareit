package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemAsRequestAnswerGetDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public Item toItem(ItemDto itemDto) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();

        return item;
    }

    public ItemDto toItemDto(Item itemUPD) {
        ItemDto itemDto = ItemDto.builder()
                .id(itemUPD.getId())
                .name(itemUPD.getName())
                .description(itemUPD.getDescription())
                .available(itemUPD.getAvailable())
                .requestId(itemUPD.getRequest() == null ? null : itemUPD.getRequest().getId())
                .build();
        return itemDto;
    }

    public List<ItemDto> toItemDtoList(List<Item> itemUPD) {
        List<ItemDto> itemDtoList = itemUPD.stream().map(i ->
                        toItemDto(i))
                .collect(Collectors.toList());
        return itemDtoList;
    }

    public ItemOwnerDto itemOwnerDto(Item item, Booking lastBooking,
                                     Booking nextBooking) {
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .nextBooking(nextBooking == null ? null : BookingMapper.fromBookingToBookingGetOwnerDto(nextBooking))
                .lastBooking(lastBooking == null ? null : BookingMapper.fromBookingToBookingGetOwnerDto(lastBooking))
                .comments(item.getComments().stream()
                        .map(comment -> CommentMapper.toCommentGetDto(comment))
                        .collect(Collectors.toList()))
                .build();

        if (itemOwnerDto.getComments() == null) {
            itemOwnerDto.setComments(new ArrayList<>());
        }
        return itemOwnerDto;
    }

    public ItemAsRequestAnswerGetDto itemToItemAsRequestAnswerGetDto(Item item) {
        return ItemAsRequestAnswerGetDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item != null && item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public List<ItemAsRequestAnswerGetDto> itemToItemAsRequestAnswerGetDtoList(List<Item> items) {
        return items.stream().map(i -> itemToItemAsRequestAnswerGetDto(i))
                .collect(Collectors.toList());
    }
}
