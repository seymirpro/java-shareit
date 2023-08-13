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
class UserDtoGetIdTest {
    @Autowired
    private JacksonTester<UserDtoGetId> json;

    @Test
    void testSerialize() throws IOException {
        UserDtoGetId userDtoGetId = UserDtoGetId.builder()
                .id(1L)
                .build();
        JsonContent<UserDtoGetId> res = json.write(userDtoGetId);
        assertThat(res).hasJsonPathNumberValue("$.id");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"id\": \"1\"}";
        ObjectContent<UserDtoGetId> userDtoGetIdObjectContent = json.parse(content);
        assertThat(userDtoGetIdObjectContent).isEqualTo(UserDtoGetId.builder()
                .id(1L)
                .build());
    }
}