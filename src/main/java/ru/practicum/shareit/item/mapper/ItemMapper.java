package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();

        return item;
    }

    public static ItemDto toItemDto(Item itemUPD) {
        ItemDto itemDto = ItemDto.builder()
                .id(itemUPD.getId())
                .name(itemUPD.getName())
                .description(itemUPD.getDescription())
                .available(itemUPD.getAvailable())
                .itemRequest(itemUPD.getRequest() != null ?
                        itemUPD.getRequest() : null)
                .build();
        return itemDto;
    }
}
