package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commons.Create;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;


import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Validated({Create.class}) @Valid ItemRequestCreateUpdateDto request) {
        return itemRequestClient.createItemRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestListByUserId(
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getItemRequestListByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        return itemRequestClient.getItemRequestExceptUserId(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable(name = "requestId") long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

}