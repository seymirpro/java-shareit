package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

@Service
public class ItemService {

    private ItemDAO itemDao;
    private UserDAO userDao;

    @Autowired
    public ItemService(ItemDAO itemDao, UserDAO userDao) {
        this.itemDao = itemDao;
        this.userDao = userDao;
    }

    public Item createItem(Long id, Item item) {
        User user = userDao.getUserByID(id);
        if (id == null || user == null){
            throw new UserDoesNotExistException();
        }

        item.setOwner(user);
        return itemDao.createItem(item);
    }
}
