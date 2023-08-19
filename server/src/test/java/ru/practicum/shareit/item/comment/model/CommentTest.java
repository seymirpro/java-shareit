package ru.practicum.shareit.item.comment.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class CommentTest {
    private Comment comment;
    private Item item;
    private User author;

    @BeforeEach
    public void setup() {
        item = new Item();
        author = new User();
        comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
    }

    @Test
    public void testId() {
        long id = 1L;
        comment.setId(id);
        Assertions.assertEquals(id, comment.getId());
    }

    @Test
    public void testText() {
        String text = "This is a comment";
        comment.setText(text);
        Assertions.assertEquals(text, comment.getText());
    }

    @Test
    public void testItem() {
        Assertions.assertEquals(item, comment.getItem());
    }

    @Test
    public void testAuthor() {
        Assertions.assertEquals(author, comment.getAuthor());
    }

    @Test
    public void testCreated() {
        LocalDateTime created = LocalDateTime.now();
        comment.setCreated(created);
        Assertions.assertEquals(created, comment.getCreated());
    }

    @Test
    public void testPrePersist() {
        comment.prePersist();
        Assertions.assertNotNull(comment.getCreated());
    }
}