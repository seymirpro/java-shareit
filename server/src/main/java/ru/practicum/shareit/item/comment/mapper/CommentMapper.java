package ru.practicum.shareit.item.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentGetDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .author(User.builder().id(commentDto.getAuthorId()).build())
                .text(commentDto.getText())
                .item(Item.builder().id(commentDto.getItemId()).build())
                .build();
    }

    public CommentGetDto toCommentGetDto(Comment comment) {
        return CommentGetDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorId(comment.getAuthor().getId())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    public List<CommentGetDto> toCommentGetDtos(Collection<Comment> comments) {
        return comments.stream().map(c -> toCommentGetDto(c)).collect(Collectors.toList());
    }
}
