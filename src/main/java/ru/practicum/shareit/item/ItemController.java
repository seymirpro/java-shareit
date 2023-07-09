package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long id, @Validated({Create.class}) @RequestBody
    @Valid @NonNull ItemDto itemDto) {
        return itemService.createItem(id, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable("id") int itemId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemDto(@RequestHeader("X-Sharer-User-Id") long id,
                              @PathVariable("id") long itemId) {
        return itemService.getItemByID(id, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long id) {
        return itemService.getAllUserItems(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchKeywords(@RequestHeader("X-Sharer-User-Id") Long id,
                                                  @RequestParam("text") String searchText) {
        return itemService.getItemsBySearchKeywords(searchText);
    }
}
