package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.utils.Create;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {


    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestGetDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @Validated({Create.class})
                                            @RequestBody ItemRequestCreateUpdateDto itemRequestCreateUpdateDto) {
        return itemRequestService.addItemRequest(userId, itemRequestCreateUpdateDto);
    }

    @GetMapping
    public List<ItemRequestGetDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestGetDto> getAllRequestsExcludingUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                                               @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemRequestService.getAllRequestsExcludingUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestGetDto getItemRequestByID(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable(name = "requestId") long requestId) {
        return itemRequestService.getItemRequestByID(userId, requestId);
    }
}
