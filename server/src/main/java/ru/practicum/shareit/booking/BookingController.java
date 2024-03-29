package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.websocket.server.PathParam;
import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookingGetDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody BookingCreateUpdateDto bookingCreateUpdateDto) {
        return bookingService.createBooking(userId, bookingCreateUpdateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId,
                                       @PathParam("approved") Boolean approved) throws AccessDeniedException {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingGetDto getBookingByBookerAndByBookingId(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                          @PathVariable Long bookingId) throws AccessDeniedException {
        return bookingService.getBookingByBookerAndByBookingId(bookerId, bookingId);
    }

    @GetMapping
    public List<BookingGetDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "20") int size
    ) {
        return bookingService.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> getAllBookingsForItemOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                          @RequestParam(defaultValue = "ALL") String state,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "20") int size) {
        return bookingService.getAllBookingsForItemOwner(ownerId, state, from, size);
    }
}
