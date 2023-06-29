package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemDoesNotExistException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
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

    public ItemDto createItem(Long id, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        checkUser(id);

        User user = userDao.getUserByID(id);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemDao.createItem(item));
    }

    public ItemDto updateItem(Long userId, Integer itemId, ItemDto itemDto) {
        checkUser(userId);
        User user = userDao.getUserByID(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        item.setOwner(user);

        Item itemUpd = getItemByID(item.getId());
        if (itemUpd == null) {
            throw new ItemDoesNotExistException();
        }

        if (itemUpd.getOwner().getId().longValue() != item.getOwner().getId().longValue()) {
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

        return ItemMapper.toItemDto(itemDao.updateItem(itemUpd));
    }

    public Item getItemByID(Integer id) {
        return itemDao.getItemByID(id);
    }

    public ItemDto getItemByID(Long userId, Integer id) {
        checkUser(userId);
        return ItemMapper.toItemDto(itemDao.getItemByID(id));
    }

    public List<ItemDto> getAllUserItems(Long id) {
        checkUser(id);
        List<Item> userItems = itemDao.getAllUserItems(id);
        List<ItemDto> itemDtos = ItemMapper.toItemDtoList(userItems);
        return itemDtos;
    }

    public List<ItemDto> getItemsBySearchKeywords(Long id, String searchText) {
        checkUser(id);
        return ItemMapper.toItemDtoList(itemDao.getItemsBySearchKeywords(searchText));
    }

    private void checkUser(Long id) {
        User user = userDao.getUserByID(id);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }
    }
}
