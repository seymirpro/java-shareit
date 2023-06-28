package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Item updateItem(Item item) {
        Item itemOld = getItemByID(item.getId());
        items.remove(itemOld);
        items.add(item);
        return item;
    }

    @Override
    public Item getItemByID(Integer id) {
        Optional<Item> item = items.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        return item.orElseGet(() -> null);
    }

    @Override
    public List<Item> getAllUserItems(Long id) {
        List<Item> userItems = items.stream()
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
                .stream()
                .filter(i -> i.getAvailable() &&
                        (i.getName().toLowerCase().contains(text.toLowerCase())
                                || i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());

        return itemsFound;
    }
}
