package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemAsRequestAnswerGetDto {
    private Long id;
    private String name;
    private String description;
    private long requestId;
    private boolean available;
}
