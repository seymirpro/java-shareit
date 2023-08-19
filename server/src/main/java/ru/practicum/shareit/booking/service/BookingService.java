package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.validator.BookingValidator;
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

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ItemRepository itemRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public BookingGetDto createBooking(Long userId, BookingCreateUpdateDto bookingDto) {
        Booking booking = BookingMapper.fromBookingCreateUpdateDtoToBooking(bookingDto);
        BookingValidator.startEndDtValidation(booking.getStart(), booking.getEnd());
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(
                ItemDoesNotExistException::new
        );
        if (item.getOwner().getId() == userId) {
            throw new ItemDoesNotExistException();
        }

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("В настоящее время данная вещь недоступна");
        }
        User booker = userRepository.findById(userId).orElseThrow(
                UserDoesNotExistException::new
        );

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);

        return BookingMapper.fromBookingToBookingGetDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingGetDto updateBooking(Long userId, Long bookingId, Boolean approved) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundBookingException::new);


        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingStatusBadRequestException();
        }

        Booking booker = bookingRepository.findByBookerIdAndBookingId(userId, bookingId);
        if (booker != null) {
            throw new BookingDoesNotExistException();
        }

        booking = bookingRepository.findByItemOwnerIdAndBookingId(userId, bookingId);
        if (booking == null) {
            throw new NotFoundBookingException();
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.fromBookingToBookingGetDto(bookingRepository.save(booking));
    }

    public BookingGetDto getBookingByBookerAndByBookingId(long userId, long bookingId) {
        User user = getUserByIdIfExistsOrThrowException(userId);
        Booking booking = getBookingByIdIfExistsOrThrowException(bookingId);
        Booking bookingRes = null;
        if (booking.getItem().getOwner().getId() != userId) {
            bookingRes = bookingRepository.findByBookerIdAndBookingId(userId, bookingId);
        } else {
            bookingRes = bookingRepository.findByItemOwnerIdAndBookingId(userId, bookingId);
        }

        if (bookingRes == null) {
            throw new BookingDoesNotExistException();
        }
        return BookingMapper.fromBookingToBookingGetDto(bookingRes);
    }

    private User getUserByIdIfExistsOrThrowException(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    private Booking getBookingByIdIfExistsOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(BookingDoesNotExistException::new);
    }

    public List<BookingGetDto> getAllBookings(Long userId, String state, int from, int size) {
        User user = getUserByIdIfExistsOrThrowException(userId);
        List<Booking> res = null;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : from, size);
        switch (state) {
            case "ALL":
                res = bookingRepository.findAllByBookerId(userId, pageable);
                break;
            case "CURRENT":
                res = bookingRepository.findCurrentBookingsByBookerId(userId, pageable);
                break;
            case "PAST":
                res = bookingRepository.findPastBookingsByBookerId(userId, pageable);
                break;
            case "FUTURE":
                res = bookingRepository.findFutureBookingsByBookerId(userId, pageable);
                break;
            case "WAITING":
                res = bookingRepository.findWaitingBookingsByBookerId(userId, pageable);
                break;
            case "REJECTED":
                res = bookingRepository.findRejectedBookingsByBookerId(userId, pageable);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown state: %s", state));
        }

        return BookingMapper.toBookingGetDtoList(res);
    }

    public List<BookingGetDto> getAllBookingsForItemOwner(long itemOwnerId, String state, int from, int size) {
        User user = getUserByIdIfExistsOrThrowException(itemOwnerId);

        Pageable pageable = PageRequest.of(from > 0 ? from / size : from, size);
        List<Booking> bookings = null;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(itemOwnerId, pageable);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemOwnerId, pageable);
                break;
            case "REJECTED":
                bookings = bookingRepository.findRejectedBookingsByItemOwnerId(itemOwnerId, pageable);
                break;
            case "WAITING":
                bookings = bookingRepository.findWaitingBookingsByItemOwnerId(itemOwnerId, pageable);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByItemOwnerId(itemOwnerId, pageable);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByItemOwnerId(itemOwnerId, pageable);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown state: %s", state));
        }
        return BookingMapper.toBookingGetDtoList(bookings);
    }
}
