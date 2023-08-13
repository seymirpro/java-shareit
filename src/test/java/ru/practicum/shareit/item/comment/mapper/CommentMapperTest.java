package ru.practicum.shareit.item.comment.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testToComment() {
        CommentDto commentDto = CommentDto.builder().build();
        commentDto.setAuthorId(1L);
        commentDto.setText("Test comment");
        commentDto.setItemId(100L);

        Comment comment = CommentMapper.toComment(commentDto);

        assertEquals(1L, comment.getAuthor().getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(100L, comment.getItem().getId());
    }

    @Test
    void testToCommentGetDto() {
        Comment comment = Comment.builder()
                .item(Item.builder().build())
                .author(User.builder().build())
                .build();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.getItem().setId(100L);
        comment.getAuthor().setId(2L);
        comment.setCreated(LocalDateTime.now());
        comment.getAuthor().setName("John Doe");

        CommentGetDto commentGetDto = CommentMapper.toCommentGetDto(comment);

        assertEquals(1L, commentGetDto.getId());
        assertEquals("Test comment", commentGetDto.getText());
        assertEquals(100L, commentGetDto.getItemId());
        assertEquals(2L, commentGetDto.getAuthorId());
        assertNotNull(commentGetDto.getCreated());
        assertEquals("John Doe", commentGetDto.getAuthorName());
    }

    @Test
    void testToCommentGetDtos() {
        Comment comment1 = Comment.builder()
                .item(Item.builder().build())
                .author(User.builder().build())
                .build();
        comment1.setId(1L);
        comment1.setText("Test comment 1");
        comment1.getItem().setId(100L);
        comment1.getAuthor().setId(2L);
        comment1.setCreated(LocalDateTime.now());
        comment1.getAuthor().setName("John Doe");

        Comment comment2 = Comment.builder()
                .item(Item.builder().build())
                .author(User.builder().build())
                .build();
        comment2.setId(2L);
        comment2.setText("Test comment 2");
        comment2.getItem().setId(200L);
        comment2.getAuthor().setId(3L);
        comment2.setCreated(LocalDateTime.now());
        comment2.getAuthor().setName("Jane Smith");

        List<CommentGetDto> commentGetDtos = CommentMapper.toCommentGetDtos(Arrays.asList(comment1, comment2));

        assertEquals(2, commentGetDtos.size());

        CommentGetDto commentGetDto1 = commentGetDtos.get(0);
        assertEquals(1L, commentGetDto1.getId());
        assertEquals("Test comment 1", commentGetDto1.getText());
        assertEquals(100L, commentGetDto1.getItemId());
        assertEquals(2L, commentGetDto1.getAuthorId());
        assertNotNull(commentGetDto1.getCreated());
        assertEquals("John Doe", commentGetDto1.getAuthorName());

        CommentGetDto commentGetDto2 = commentGetDtos.get(1);
        assertEquals(2L, commentGetDto2.getId());
        assertEquals("Test comment 2", commentGetDto2.getText());
        assertEquals(200L, commentGetDto2.getItemId());
        assertEquals(3L, commentGetDto2.getAuthorId());
        assertEquals("Jane Smith", commentGetDto2.getAuthorName());
    }
}