package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestCreateUpdateDto itemRequestCreateUpdateDto) {
        return ItemRequest.builder()
                .description(itemRequestCreateUpdateDto.getDescription())
                .build();
    }

    public ItemRequestGetDto toItemRequestGetDto(ItemRequest newItemRequest) {
        return ItemRequestGetDto.builder()
                .id(newItemRequest.getId())
                .description(newItemRequest.getDescription())
                .created(newItemRequest.getCreated())
                .requestId(newItemRequest.getRequestor() == null ? null : newItemRequest.getRequestor().getId())
                .items(ItemMapper.itemToItemAsRequestAnswerGetDtoList(
                        newItemRequest.getItems() == null ||
                                newItemRequest.getItems().isEmpty() ? List.of() :
                                newItemRequest.getItems()
                                        .stream()
                                        .collect(Collectors.toList())
                ))
                .build();
    }

    public static List<ItemRequestGetDto> toItemRequestGetDtoList(List<ItemRequest> itemRequestsList) {
        return itemRequestsList
                .stream()
                .map(itemRequest -> toItemRequestGetDto(itemRequest))
                .collect(Collectors.toList());
    }
}
