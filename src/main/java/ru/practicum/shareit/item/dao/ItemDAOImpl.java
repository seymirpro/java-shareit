package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDAOImpl implements ItemDAO {
    private static int id = 0;
    private List<Item> items = new ArrayList<>();

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.add(item);
        return item;
    }
}
