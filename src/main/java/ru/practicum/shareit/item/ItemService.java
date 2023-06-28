package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemDoesNotExistException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
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
        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }

        item.setOwner(user);
        return itemDao.createItem(item);
    }

    public Item updateItem(Long id, Item item) {
        User user = userDao.getUserByID(id);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }

        item.setOwner(user);
        log.info("line {}", 45);

        Item itemUpd = getItemByID(item.getId());
        if (itemUpd == null) {
            throw new ItemDoesNotExistException();
        }

        log.info("line {}", 52);

        if (itemUpd.getOwner().getId() != item.getOwner().getId()) {
            throw new NotItemOwnerException();
        }

        if (item.getAvailable() != null && !item.getAvailable().equals(itemUpd.getAvailable())) {
            itemUpd.setAvailable(item.getAvailable());
        }



        if (item.getName() != null && !item.getName().equals(itemUpd.getName())) {
            itemUpd.setName(item.getName());
        }


        if (item.getDescription() != null &&
                !item.getDescription().equals(itemUpd.getDescription())) {
            itemUpd.setDescription(item.getDescription());
        }



        return itemDao.updateItem(itemUpd);
    }

    public Item getItemByID(Integer id) {
        return itemDao.getItemByID(id);
    }

    public Item getItemByID(Long userId, Integer id) {
        User user = userDao.getUserByID(userId);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }

        return itemDao.getItemByID(id);
    }

    public List<Item> getAllUserItems(Long id) {
        User user = userDao.getUserByID(id);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }

        List<Item> userItems = itemDao.getAllUserItems(id);
        return userItems;
    }

    public List<Item> getItemsBySearchKeywords(Long id, String searchText) {
        User user = userDao.getUserByID(id);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }
        return itemDao.getItemsBySearchKeywords(searchText);
    }
}
