package ru.practicum.shareit.item.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.model.Comment;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .authorId(commentDto.getAuthorId())
                .text(commentDto.getText())
                .itemId(commentDto.getItemId())
                .build();
    }

    public CommentGetDto toCommentGetDto(Comment comment) {
        return CommentGetDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .authorId(comment.getAuthorId())
                .created(comment.getCreated())
                .build();
    }
}
