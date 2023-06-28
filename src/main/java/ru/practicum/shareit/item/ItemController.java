package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long id, @Valid
    @RequestBody @NonNull ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Item itemUPD = itemService.createItem(id, item);
        return ItemMapper.toItemDto(itemUPD);
    }

    @PatchMapping
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long id, ItemDto itemDto) {
        return null;
    }
}
