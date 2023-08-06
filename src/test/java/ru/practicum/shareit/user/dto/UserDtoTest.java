package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.email");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@mail.ru");
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"id\": 1,\"name\":\"user\",\"email\":\"user@mail.ru\"}";
        ObjectContent<UserDto> result = json.parse(content);
        assertThat(result).isEqualTo(UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build());
    }
}