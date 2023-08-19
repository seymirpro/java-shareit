package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commons.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @NotNull @RequestBody @Validated({Create.class}) @Valid ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long itemId,
            @NotNull @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.updateItem(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemListByOwnerId(
            @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemClient.getItemListByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text) {
        return itemClient.searchItemByNameOrDescription(text.toLowerCase());
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(commentDto, bookerId, itemId);
    }

}
