package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

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
        return itemService.createItem(id, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("id") Integer itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemDto(@RequestHeader("X-Sharer-User-Id") Long id,
                              @PathVariable("id") Integer itemId) {
        return itemService.getItemByID(id, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.getAllUserItems(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchKeywords(@RequestHeader("X-Sharer-User-Id") Long id,
                                                  @RequestParam("text") String searchText) {
        return itemService.getItemsBySearchKeywords(id, searchText);
    }
}
