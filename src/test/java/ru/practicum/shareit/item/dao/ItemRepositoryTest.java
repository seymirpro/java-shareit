package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item item1 = Item.builder()
                .id(1)
                .name("item1")
                .description("item1 description")
                .available(true)
                .build();
        itemRepository.save(item1);
    }

    @Test
    void testFindItemsByDescriptionContainingIgnoreCase() {
        List<Item> items = itemRepository
                .findItemsByDescriptionContainingIgnoreCase("Item1 DescRipTion");
        assertEquals(1, items.get(0).getId());
        assertEquals("item1", items.get(0).getName());
    }

    @Test
    void testFindByOwnerId() {
        Long ownerId = 5L;
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        assertTrue(items.isEmpty());
    }

    @Test
    void testFindByOwnerIdAndId() {
        Long ownerId = 340L;
        Long itemId = 344L;
        Item item = itemRepository.findByOwnerIdAndId(ownerId, itemId);
        assertTrue(item == null);
    }
}