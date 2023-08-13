package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentGetDtoTest {
    @Test
    public void testId() {
        CommentGetDto comment = CommentGetDto.builder().build();
        Long id = 123L;
        comment.setId(id);
        assertEquals(id, comment.getId());
    }

    @Test
    public void testText() {
        CommentGetDto comment = CommentGetDto.builder().build();
        String text = "Sample text";
        comment.setText(text);
        assertEquals(text, comment.getText());
    }

    @Test
    public void testItemId() {
        CommentGetDto comment = CommentGetDto.builder().build();
        Long itemId = 456L;
        comment.setItemId(itemId);
        assertEquals(itemId, comment.getItemId());
    }

    @Test
    public void testAuthorId() {
        CommentGetDto comment = CommentGetDto.builder().build();
        Long authorId = 789L;
        comment.setAuthorId(authorId);
        assertEquals(authorId, comment.getAuthorId());
    }

    @Test
    public void testAuthorName() {
        CommentGetDto comment = CommentGetDto.builder().build();
        String authorName = "John Doe";
        comment.setAuthorName(authorName);
        assertEquals(authorName, comment.getAuthorName());
    }

    @Test
    public void testCreated() {
        CommentGetDto comment = CommentGetDto.builder().build();
        LocalDateTime created = LocalDateTime.now();
        comment.setCreated(created);
        assertEquals(created, comment.getCreated());
    }
}