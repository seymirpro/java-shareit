package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDAO {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemByID(Integer id);

    List<Item> getAllUserItems(Long id);

    List<Item> getItemsBySearchKeywords(String text);
}
