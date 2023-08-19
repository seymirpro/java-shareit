package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.request.ItemRequestDoesNotExistException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.request.dto.ItemRequestCreateUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        itemRequestService =
                new ItemRequestService(itemRequestRepository, userRepository);
    }

    @Test
    void testAddItemRequestShouldThrowException() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class,
                () -> itemRequestService.addItemRequest(1L,
                        ItemRequestCreateUpdateDto.builder().build()));
    }

    @Test
    void testAddItemRequestShould() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.ru")
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(user));
        ItemRequestCreateUpdateDto itemRequestCreateUpdateDto = ItemRequestCreateUpdateDto
                .builder()
                .description("description")
                .build();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestCreateUpdateDto);
        itemRequest.setId(1);
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestGetDto itemRequestGetDto = itemRequestService.addItemRequest(1L, itemRequestCreateUpdateDto);
        assertNotNull(itemRequestGetDto);
        assertEquals(1, itemRequestGetDto.getId());
    }

    @Test
    @SneakyThrows
    void testGetItemRequests() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.ru")
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(user));
        when(itemRequestRepository.findUserItemRequestsWithItemOptions(any(User.class),
                any(Sort.class)))
                .thenReturn(new ArrayList<>());
        assertEquals(0, itemRequestService.getItemRequests(1).size());
    }

    @Test
    @SneakyThrows
    void testGetAllRequestsExcludingUser() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.ru")
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(user));

        when(itemRequestRepository.getAllRequestsExcludingUser(any(User.class), any(Pageable.class)))
                .thenReturn(new ArrayList<>());
        assertEquals(0, itemRequestService.getAllRequestsExcludingUser(1L, 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetItemRequestByID() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.ru")
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(user));
        when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new ItemRequestDoesNotExistException());
        assertThrows(ItemRequestDoesNotExistException.class,
                () -> itemRequestService.getItemRequestByID(1L, 2L));
    }
}