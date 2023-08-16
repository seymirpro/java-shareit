package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapperTest {

    @Test
    void toItem_shouldMapItemDtoToItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();


        Item item = ItemMapper.toItem(itemDto);

        Assertions.assertEquals(itemDto.getName(), item.getName());
        Assertions.assertEquals(itemDto.getDescription(), item.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), item.getAvailable());
    }

    @Test
    void toItemDto_shouldMapItemToItemDto() {
        Item item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        ItemDto itemDto = ItemMapper.toItemDto(item);

        Assertions.assertEquals(item.getName(), itemDto.getName());
        Assertions.assertEquals(item.getDescription(), itemDto.getDescription());
        Assertions.assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void toItemDtoList_shouldMapItemListToItemDtoList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(Item.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .build());
        itemList.add(Item.builder()
                .name("Item 2")
                .description("Description 2")
                .available(false)
                .build());


        List<ItemDto> itemDtoList = ItemMapper.toItemDtoList(itemList);

        Assertions.assertEquals(itemList.size(), itemDtoList.size());
        Assertions.assertEquals(itemList.get(0).getName(), itemDtoList.get(0).getName());
        Assertions.assertEquals(itemList.get(0).getDescription(), itemDtoList.get(0).getDescription());
        Assertions.assertEquals(itemList.get(0).getAvailable(), itemDtoList.get(0).getAvailable());
        Assertions.assertEquals(itemList.get(1).getName(), itemDtoList.get(1).getName());
        Assertions.assertEquals(itemList.get(1).getDescription(), itemDtoList.get(1).getDescription());
        Assertions.assertEquals(itemList.get(1).getAvailable(), itemDtoList.get(1).getAvailable());
    }
}