package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long id, @Validated({Create.class}) @RequestBody
    @Valid @NonNull ItemDto itemDto) {
        return itemServiceImpl.createItem(id, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable("id") int itemId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemServiceImpl.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemOwnerDto getItemDto(@RequestHeader("X-Sharer-User-Id") long id,
                                   @PathVariable("id") long itemId) {
        ItemOwnerDto itemOwnerDto = itemServiceImpl.getItemByID(id, itemId);
        return itemOwnerDto;
    }

    @GetMapping
    public List<ItemOwnerDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long id) {
        return itemServiceImpl.getAllUserItems(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchKeywords(@RequestParam("text") String searchText) {
        return itemServiceImpl.getItemsBySearchKeywords(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public CommentGetDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable long itemId,
                                    @RequestBody CommentDto commentDto
    ) {
        return itemServiceImpl.addComment(userId, itemId, commentDto);
    }
}
