package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

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

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long id,
                              @PathVariable("id") Integer itemId,
                              @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        Item itemUPD = itemService.updateItem(id, item);
        return ItemMapper.toItemDto(itemUPD);
    }

    @GetMapping("/{id}")
    public ItemDto getItemDto(@RequestHeader("X-Sharer-User-Id") Long id,
                              @PathVariable("id") Integer itemId) {

        Item item = itemService.getItemByID(id, itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        List<Item> items = itemService.getAllUserItems(id);
        List<ItemDto> itemDtos = ItemMapper.toItemDtoList(items);
        return itemDtos;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchKeywords(@RequestHeader("X-Sharer-User-Id") Long id,
                                                  @RequestParam("text") String searchText) {
        List<Item> itemsFound = itemService.getItemsBySearchKeywords(id, searchText);
        List<ItemDto> itemsDtosFound = ItemMapper.toItemDtoList(itemsFound);
        return itemsDtosFound;
    }
}
