package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommentDtoTest {
    @Test
    public void testSetText() {
        String expectedText = "Sample text";
        CommentDto commentDto = CommentDto.builder().build();

        commentDto.setText(expectedText);
        String actualText = commentDto.getText();

        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    public void testSetItemId() {
        Long expectedItemId = 1L;
        CommentDto commentDto = CommentDto.builder().build();

        commentDto.setItemId(expectedItemId);
        Long actualItemId = commentDto.getItemId();

        Assertions.assertEquals(expectedItemId, actualItemId);
    }

    @Test
    public void testSetAuthorId() {
        Long expectedAuthorId = 2L;
        CommentDto commentDto = CommentDto.builder().build();

        commentDto.setAuthorId(expectedAuthorId);
        Long actualAuthorId = commentDto.getAuthorId();

        Assertions.assertEquals(expectedAuthorId, actualAuthorId);
    }
}