package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.booking.BookingDoesNotExistException;
import ru.practicum.shareit.exception.booking.BookingStatusBadRequestException;
import ru.practicum.shareit.exception.booking.NotFoundBookingException;
import ru.practicum.shareit.exception.item.ItemDoesNotExistException;
import ru.practicum.shareit.exception.item.ItemNotAvailableException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, itemRepository, userRepository);
    }

    @Test
    @SneakyThrows
    void testCreateBookingShouldThrowExceptionIfItemDoesNotExist() {
        BookingCreateUpdateDto bookingDto = BookingCreateUpdateDto
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .itemId(1L)
                .build();
        when(itemRepository.findById(anyLong()))
                .thenThrow(new ItemDoesNotExistException());
        assertThrows(ItemDoesNotExistException.class,
                () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    @SneakyThrows
    void testCreateBookingShouldThrowExceptionIfItemOwnerIdIsNotEqualToUserId() {
        BookingCreateUpdateDto bookingDto = BookingCreateUpdateDto
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .itemId(1L)
                .build();
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(item));
        assertThrows(ItemDoesNotExistException.class,
                () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    @SneakyThrows
    void testCreateBookingShouldThrowExceptionIfItemIsNotAvailable() {
        BookingCreateUpdateDto bookingDto = BookingCreateUpdateDto
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .itemId(1L)
                .build();
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(false)
                .owner(user)
                .build();
        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(item));
        assertThrows(ItemNotAvailableException.class,
                () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @SneakyThrows
    void testCreateBooking() {
        BookingCreateUpdateDto bookingDto = BookingCreateUpdateDto
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .itemId(1L)
                .build();
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        Booking booking = BookingMapper.fromBookingCreateUpdateDtoToBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder()
                .id(1)
                .build()));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingGetDto bookingGetDto = bookingService.createBooking(2L, bookingDto);
        assertNotNull(bookingGetDto);
    }

    @Test
    @SneakyThrows
    void testUpdateBookingShouldThrowExceptionWhenBookingDoesNotExist() {
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new BookingDoesNotExistException());
        assertThrows(BookingDoesNotExistException.class, () -> bookingService.updateBooking(1L,
                2L, true));
    }

    @Test
    @SneakyThrows
    void testUpdateBookingShouldThrowExceptionWhenStatusApproved() {
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        Booking booking = Booking
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(booking));

        assertThrows(BookingStatusBadRequestException.class, () -> bookingService.updateBooking(1L,
                2L, true));
    }

    @Test
    @SneakyThrows
    void testUpdateBookingShouldThrowExceptionWhenBookerIsNotNull() {
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        Booking booking = Booking
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
        when(bookingRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(booking));
        when(bookingRepository.findByBookerIdAndBookingId(anyLong(), anyLong()))
                .thenAnswer(invocationOnMock -> Booking.builder().build());
        assertThrows(BookingDoesNotExistException.class, () -> bookingService.updateBooking(1L,
                2L, true));
    }

    @Test
    @SneakyThrows
    void testUpdateBookingShouldThrowExceptionWhenBookingIsNull() {
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        Booking booking = Booking
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
        when(bookingRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(booking));
        when(bookingRepository.findByBookerIdAndBookingId(anyLong(), anyLong()))
                .thenReturn(null);
        when(bookingRepository.findByItemOwnerIdAndBookingId(anyLong(), anyLong()))
                .thenReturn(null);
        assertThrows(NotFoundBookingException.class, () -> bookingService.updateBooking(1L,
                2L, true));
    }

    @Test
    @SneakyThrows
    void testUpdateBooking() {
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        Booking booking = Booking
                .builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
        when(bookingRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(booking));
        when(bookingRepository.findByBookerIdAndBookingId(anyLong(), anyLong()))
                .thenReturn(null);
        when(bookingRepository.findByItemOwnerIdAndBookingId(anyLong(), anyLong()))
                .thenReturn(booking);
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocationOnMock -> booking);
        BookingGetDto bookingGetDto = bookingService.updateBooking(1L, 1L, true);
        assertNotNull(bookingGetDto);
    }

    @Test
    @SneakyThrows
    void testGetBookingByBookerAndByBookingIdShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(6L))
                .thenThrow(new UserDoesNotExistException());
        assertThrows(UserDoesNotExistException.class,
                () -> bookingService.getBookingByBookerAndByBookingId(6L, anyLong()));
    }

    @Test
    @SneakyThrows
    void testGetBookingByBookerAndByBookingIdShouldThrowExceptionWhenBookingDoesNotExist() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder().build()));
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new BookingDoesNotExistException());
        assertThrows(BookingDoesNotExistException.class,
                () -> bookingService.getBookingByBookerAndByBookingId(1L, 2L));
    }

    @Test
    @SneakyThrows
    void testGetBookingByBookerAndByBookingIdWhenUserIsNotOwner() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        when(bookingRepository.findById(anyLong())).thenAnswer(invocationOnMock -> Optional.of(booking));
        when(bookingRepository.findByBookerIdAndBookingId(anyLong(), anyLong()))
                .thenAnswer(invocationOnMock -> booking);
        BookingGetDto bookingGetDto = bookingService.getBookingByBookerAndByBookingId(90L, 1L);
        assertNotNull(bookingGetDto);
    }

    @Test
    @SneakyThrows
    void testGetAllBookings() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findAllByBookerId(anyLong(),
                any(Pageable.class))).thenAnswer(invocationOnMock -> bookings);
        assertEquals(1, bookingService.getAllBookings(1L, "ALL", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsPast() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findPastBookingsByBookerId(anyLong(),
                any(Pageable.class))).thenAnswer(invocationOnMock -> new ArrayList<>());
        assertEquals(0, bookingService.getAllBookings(1L, "PAST", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsCurrent() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findCurrentBookingsByBookerId(anyLong(),
                any(Pageable.class))).thenAnswer(invocationOnMock -> new ArrayList<>());
        assertEquals(0, bookingService.getAllBookings(1L, "CURRENT", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsFuture() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findFutureBookingsByBookerId(anyLong(),
                any(Pageable.class))).thenAnswer(invocationOnMock -> bookings);
        assertEquals(1, bookingService.getAllBookings(1L, "FUTURE", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsWaiting() {
        User owner = User.builder().id(1).name("user").email("user@mail.ru").build();
        User booker = User.builder().id(2).name("item 2").email("email 2").build();
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(owner));
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findWaitingBookingsByBookerId(anyLong(),
                any(Pageable.class))).thenAnswer(invocationOnMock -> new ArrayList<>());
        assertEquals(0, bookingService.getAllBookings(1L, "WAITING", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsRejected() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findRejectedBookingsByBookerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertEquals(0, bookingService.getAllBookings(1L, "REJECTED", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsShouldThrowException() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllBookings(1L, "REJECTsssED", 1, 2).size());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsForItemOwnerAll() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertTrue(bookingService.getAllBookingsForItemOwner(1L, "ALL", 1, 2).isEmpty());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsForItemOwnerFuture() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findFutureBookingsByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertTrue(bookingService.getAllBookingsForItemOwner(1L, "FUTURE", 1, 2).isEmpty());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsForItemOwnerRejected() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findRejectedBookingsByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertTrue(bookingService.getAllBookingsForItemOwner(1L, "REJECTED", 1, 2).isEmpty());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsForItemOwnerWaiting() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findWaitingBookingsByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertTrue(bookingService.getAllBookingsForItemOwner(1L, "WAITING", 1, 2).isEmpty());
    }

    @Test
    @SneakyThrows
    void testGetAllBookingsForItemOwnerPast() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .id(1)
                        .name("user")
                        .email("user@mail")
                        .build()));
        when(bookingRepository.findPastBookingsByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenAnswer(invocationOnMock -> new ArrayList<>());
        assertTrue(bookingService.getAllBookingsForItemOwner(1L, "PAST", 1, 2).isEmpty());
    }
}