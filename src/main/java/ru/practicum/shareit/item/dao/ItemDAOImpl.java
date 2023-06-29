package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDAOImpl implements ItemDAO {
    private static Integer id = 0;
    private Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item itemOld = getItemByID(item.getId());
        items.remove(itemOld.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemByID(Integer id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllUserItems(Long id) {
        List<Item> userItems = items
                .values()
                .stream()
                .filter(i ->
                        i.getOwner().getId().longValue() == id.longValue())
                .collect(Collectors.toList());
        return userItems;
    }

    @Override
    public List<Item> getItemsBySearchKeywords(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        List<Item> itemsFound = items
                .values()
                .stream()
                .filter(i -> i.getAvailable() &&
                        (i.getName().toLowerCase().contains(text.toLowerCase())
                                || i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());

        return itemsFound;
    }
}
