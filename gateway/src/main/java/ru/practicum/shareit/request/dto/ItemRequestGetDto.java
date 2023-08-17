package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemAsRequestAnswerGetDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestGetDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private Long requestId;
    private List<ItemAsRequestAnswerGetDto> items;
}
