package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemDoesNotExistException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemDto createItem(long id, ItemDto itemDto) {
        log.debug(itemDto.toString());
        System.out.printf(itemDto.toString());
        Item item = ItemMapper.toItem(itemDto);
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(long userId, int itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        item.setOwner(user);

        Item itemFromDB = itemRepository.findById(item.getId())
                .orElseThrow(ItemDoesNotExistException::new);

        if (itemFromDB.getOwner().getId() != item.getOwner().getId()) {
            throw new NotItemOwnerException();
        }

        if (item.getAvailable() != null && !item.getAvailable().equals(itemFromDB.getAvailable())) {
            itemFromDB.setAvailable(item.getAvailable());
        }

        if (item.getName() != null && !item.getName().equals(itemFromDB.getName())) {
            itemFromDB.setName(item.getName());
        }

        if (item.getDescription() != null &&
                !item.getDescription().equals(itemFromDB.getDescription())) {
            itemFromDB.setDescription(item.getDescription());
        }

        return ItemMapper.toItemDto(itemRepository.save(itemFromDB));
    }

    public ItemDto getItemByID(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemDoesNotExistException::new);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> getAllUserItems(Long ownerId) {
        List<Item> userItems = itemRepository.findByOwnerId(ownerId);;
        List<ItemDto> itemDtos = ItemMapper.toItemDtoList(userItems);
        return itemDtos;
    }

    public List<ItemDto> getItemsBySearchKeywords(String searchText) {
        return ItemMapper
                .toItemDtoList(
                        itemRepository.findItemsByDescriptionContainingIgnoreCase(searchText)
                );
    }
}
