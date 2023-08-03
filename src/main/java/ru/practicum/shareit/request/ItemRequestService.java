package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.request.ItemRequestDoesNotExistException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {
    private ItemRequestRepository itemRequestRepository;
    private UserRepository userRepository;

    @Autowired
    public ItemRequestService(final ItemRequestRepository itemRequestRepository,
                              final UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    public ItemRequestGetDto addItemRequest(long userId, ItemRequestCreateUpdateDto itemRequestCreateUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestCreateUpdateDto);
        itemRequest.setRequestor(user);
        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestGetDto(newItemRequest);
    }

    public List<ItemRequestGetDto> getItemRequests(long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);

        return ItemRequestMapper.toItemRequestGetDtoList(itemRequestRepository.findUserItemRequestsWithItemOptions(user,
                Sort.by(Sort.Direction.DESC, "created")));
    }

    public List<ItemRequestGetDto> getAllRequestsExcludingUser(long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").ascending());
        return itemRequestRepository.getAllRequestsExcludingUser(user, pageable)
                .stream()
                .map(ItemRequestMapper::toItemRequestGetDto)
                .collect(Collectors.toList());
    }

    public ItemRequestGetDto getItemRequestByID(long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
        return ItemRequestMapper.toItemRequestGetDto(
                itemRequestRepository.findById(requestId)
                        .orElseThrow(ItemRequestDoesNotExistException::new));
    }
}
