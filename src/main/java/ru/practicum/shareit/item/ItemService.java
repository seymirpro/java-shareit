package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

//@Service
@Slf4j
public class ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;

    //@Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemDto createItem(Long id, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        checkUser(id);

        User user = userRepository.findById(id).get();
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Long userId, Integer itemId, ItemDto itemDto) {
        checkUser(userId);
        Optional<User> user = userRepository.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(Long.valueOf(itemId));
        item.setOwner(user.orElseGet(null));

        Item itemUpd = itemRepository.findById(item.getId()).get();
        if (itemUpd == null) {
            throw new ItemDoesNotExistException();
        }

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

        return ItemMapper.toItemDto(itemRepository.save(itemUpd));
    }

    public ItemDto getItemByID(Long userId, Integer id) {
        checkUser(userId);
        return ItemMapper.toItemDto(itemRepository.findById(Long.valueOf(id)).orElseGet(null));
    }

    public List<ItemDto> getAllUserItems(Long id) {
        checkUser(id);
        List<Item> userItems = itemRepository.findAllById(Collections.singleton(id));
        List<ItemDto> itemDtos = ItemMapper.toItemDtoList(userItems);
        return itemDtos;
    }

    public List<ItemDto> getItemsBySearchKeywords(Long id, String searchText) {
        checkUser(id);
        //return ItemMapper.toItemDtoList(itemRepository.getItemsBySearchKeywords(searchText));
        return null;
    }

    private void checkUser(Long id) {
        User user = userRepository.findById(id).orElseGet(null);

        if (id == null || user == null) {
            throw new UserDoesNotExistException();
        }
    }
}
