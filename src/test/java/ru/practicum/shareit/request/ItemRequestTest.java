package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestTest {
    @Test
    void prePersistShouldSetCreatedTimestamp() {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.prePersist();

        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getYear(), itemRequest.getCreated().getYear());
        assertEquals(now.getMonth(), itemRequest.getCreated().getMonth());
        assertEquals(now.getDayOfMonth(), itemRequest.getCreated().getDayOfMonth());
    }

    @Test
    void constructorWithAllArgumentsShouldSetFields() {
        long id = 1;
        String description = "Test Description";
        User requestor = new User();
        LocalDateTime created = LocalDateTime.now();
        Set<Item> items = new HashSet<>();

        ItemRequest itemRequest = new ItemRequest(id, description, requestor, created, items);

        assertEquals(id, itemRequest.getId());
        assertEquals(description, itemRequest.getDescription());
        assertEquals(requestor, itemRequest.getRequestor());
        assertEquals(created, itemRequest.getCreated());
        assertEquals(items, itemRequest.getItems());
    }

    @Test
    void settersAndGettersShouldSetAndReturnCorrectValues() {
        ItemRequest itemRequest = new ItemRequest();
        long id = 1;
        String description = "Test Description";
        User requestor = new User();
        LocalDateTime created = LocalDateTime.now();
        Set<Item> items = new HashSet<>();

        itemRequest.setId(id);
        itemRequest.setDescription(description);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);
        itemRequest.setItems(items);

        assertEquals(id, itemRequest.getId());
        assertEquals(description, itemRequest.getDescription());
        assertEquals(requestor, itemRequest.getRequestor());
        assertEquals(created, itemRequest.getCreated());
        assertEquals(items, itemRequest.getItems());
    }
}