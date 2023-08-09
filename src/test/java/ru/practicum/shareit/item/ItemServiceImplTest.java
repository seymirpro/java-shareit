package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.comment.CommentBadRequestException;
import ru.practicum.shareit.exception.item.ItemDoesNotExistException;
import ru.practicum.shareit.exception.item.NotItemOwnerException;
import ru.practicum.shareit.exception.request.ItemRequestDoesNotExistException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.item.comment.dao.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    public void setSup() {
        Mockito.reset(userRepository);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository,
                commentRepository, itemRequestRepository);
    }

    @Test
    @SneakyThrows
    void testCreateItemThrowsExceptionWhenUserDoesNotExists() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class,
                () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @SneakyThrows
    void testShouldThrowExceptionWhenItemRequestIsNotNullAndDoesNotExist() {
        UserDto userDto1 = UserDto.builder()
                .name("user 1")
                .email("user1@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(UserMapper.toUser(userDto1)));
        when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new ItemRequestDoesNotExistException());
        ItemRequest itemRequest = ItemRequest.builder().id(1).build();
        itemRequest.setRequestor(UserMapper.toUser(userDto1));
        itemDto.setRequestId(itemRequest.getId());
        assertThrows(ItemRequestDoesNotExistException.class,
                () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @SneakyThrows
    void testCreateItem() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("user 1")
                .email("user1@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(UserMapper.toUser(userDto1)));
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1)
                .build();
        itemRequest.setRequestor(UserMapper.toUser(userDto1));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));
        itemDto.setRequestId(itemRequest.getId());
        Item savedItem = ItemMapper.toItem(itemDto);
        when(itemRepository.save(any(Item.class)))
                .thenReturn(savedItem);
        ItemDto savedItemDto = itemService.createItem(1, itemDto);
        savedItemDto.setRequestId(itemRequest.getId());
        assertEquals(1L, savedItemDto.getRequestId());
    }

    @Test
    @SneakyThrows
    void testShouldReturnExceptionWhenUserDoesNotExist() {
        ItemDto itemDto = ItemDto.builder()
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class,
                () -> itemService.updateItem(1, 2, itemDto));
    }

    @Test
    @SneakyThrows
    void testUpdateItemShouldThrowExceptionIfItemDoesNotExist() {
        ItemDto itemDto = ItemDto.builder()
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1L)
                        .name("user 1")
                        .email("user1@mail.ru")
                        .build()));
        when(itemRepository.findById(anyLong()))
                .thenThrow(new ItemDoesNotExistException());
        assertThrows(ItemDoesNotExistException.class, () ->
                itemService.updateItem(1, 2, itemDto));
    }

    @Test
    @SneakyThrows
    void testUpdateItemShouldThrowExceptionWhenUserIsNotItemOwner() {
        ItemDto itemDto = ItemDto.builder()
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(
                        invocationOnMock -> Optional.of(User.builder()
                                .id(1L)
                                .name("user 1")
                                .email("user1@mail.ru")
                                .build())
                );

        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(Item.builder()
                        .name("item 1")
                        .description("item 1 description")
                        .available(true)
                        .owner(User.builder().id(2L).build())
                        .build()));
        assertThrows(NotItemOwnerException.class,
                () -> itemService.updateItem(1, 2, itemDto));
    }

    @Test
    @SneakyThrows
    void testUpdateItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("item 1 update")
                .description("item 1 description update")
                .available(false)
                .requestId(null)
                .build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(
                        invocationOnMock -> Optional.of(User.builder()
                                .id(1L)
                                .name("user 1")
                                .email("user1@mail.ru")
                                .build())
                );

        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(Item.builder()
                        .id(1)
                        .name("item 1")
                        .description("item 1 description")
                        .available(true)
                        .owner(User.builder().id(1L).name("user").email("user@mail.ru").build())
                        .build()));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocationOnMock -> Item.builder()
                .id(1)
                .name("item 1 update")
                .description("item 1 description update")
                .available(false)
                .build());
        ItemDto itemDtoUpdated = itemService.updateItem(1, 1, itemDto);
        assertEquals(itemDto.getName(), itemDtoUpdated.getName());
        assertEquals(itemDto.getDescription(), itemDtoUpdated.getDescription());
        assertEquals(itemDto.getAvailable(), itemDtoUpdated.getAvailable());
    }

    @Test
    @SneakyThrows
    void testGetItemsBySearchKeywords() {
        when(itemRepository.findItemsByDescriptionContainingIgnoreCase(anyString()))
                .thenReturn(List.of());
        List<ItemDto> itemDtos = itemService.getItemsBySearchKeywords("sdfvfvfv");
        assertTrue(itemDtos.isEmpty());
        verify(itemRepository, times(1)).findItemsByDescriptionContainingIgnoreCase(anyString());
    }

    @Test
    @SneakyThrows
    void testGetItemByID() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1L)
                        .name("user 1")
                        .email("user1@mail.ru")
                        .build()));

        Item item = Item.builder()
                .id(1)
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .owner(User.builder().id(1L).name("user").email("user@mail.ru").build())
                .comments(new ArrayList<>())
                .build();
        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(item));

        when(bookingRepository.findNextBookingByItemOwnerId(anyLong(), anyLong()))
                .thenAnswer(
                        invocationOnMock -> {
                            List<Booking> bookings = new ArrayList<>();
                            Booking booking = Booking.builder()
                                    .id(1)
                                    .start(LocalDateTime.now().minusDays(12))
                                    .end(LocalDateTime.now().minusDays(5))
                                    .booker(User.builder().id(2).build())
                                    .build();
                            bookings.add(booking);
                            return bookings;
                        }
                );

        when(bookingRepository.findLastBookingByItemOwnerId(anyLong(), anyLong()))
                .thenAnswer(
                        invocationOnMock -> {
                            List<Booking> bookings = new ArrayList<>();
                            Booking booking = Booking.builder()
                                    .id(1)
                                    .start(LocalDateTime.now().minusDays(12))
                                    .end(LocalDateTime.now().plusDays(20))
                                    .booker(User.builder().id(2).build())
                                    .build();
                            bookings.add(booking);
                            return bookings;
                        }
                );
        ItemOwnerDto ownerDto = itemService.getItemByID(1, 1);
        assertNotNull(ownerDto);
    }

    @Test
    @SneakyThrows
    void testGetAllUserItems() {
        Item item = Item.builder()
                .id(1)
                .name("item 1")
                .description("item 1 description")
                .available(true)
                .owner(User.builder().id(1L).name("user").email("user@mail.ru").build())
                .comments(new ArrayList<>())
                .build();
        when(itemRepository.findByOwnerId(anyLong()))
                .thenReturn(List.of(item));
        when(bookingRepository.findBookingsByItemOwnerId(anyLong()))
                .thenReturn(List.of(
                        Booking.builder()
                                .id(1)
                                .status(Status.APPROVED)
                                .start(LocalDateTime.now().minusDays(12))
                                .end(LocalDateTime.now().minusDays(20))
                                .booker(User.builder().id(2).build())
                                .item(item).build(),
                        Booking.builder().id(1)
                                .status(Status.APPROVED)
                                .start(LocalDateTime.now().minusDays(3))
                                .end(LocalDateTime.now().plusDays(20))
                                .booker(User.builder().id(2)
                                        .build())
                                .item(item)
                                .build()));
        List<ItemOwnerDto> itemOwnerDtos = itemService.getAllUserItems(1L);
        assertFalse(itemOwnerDtos.isEmpty());
    }

    @Test
    @SneakyThrows
    void testCommentWithEmptyTextThrowException() {
        CommentDto commentDto = CommentDto.builder().build();
        assertThrows(CommentBadRequestException.class, () ->
                itemService.addComment(1L, 2L, commentDto));
    }
}