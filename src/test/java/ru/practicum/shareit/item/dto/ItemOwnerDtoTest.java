package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemOwnerDtoTest {
    @Autowired
    private JacksonTester<ItemOwnerDto> json;

    @Test
    void testSerialize() throws IOException {
        ItemOwnerDto dto = ItemOwnerDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(false)
                .requestId(4L)
                .comments(new ArrayList<>())
                .nextBooking(null)
                .lastBooking(null)
                .build();

        JsonContent<ItemOwnerDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).hasJsonPathNumberValue("$.requestId");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(4);
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"id\":\"1\",\"name\":\"itemName\",\"description\":\"itemDescription\"" +
                ",\"available\":\"false\"}";

        ObjectContent<ItemOwnerDto> result = json.parse(content);

        assertThat(result).isEqualTo(ItemOwnerDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(false)
                .comments(null)
                .nextBooking(null)
                .lastBooking(null)
                .build());
    }
}