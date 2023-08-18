package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.commons.Create;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestBody @Validated({Create.class}) @Valid BookingCreateUpdateDto bookingRequestDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(
            @PathVariable long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookedItems(
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        return bookingClient.getOwnerBookedItems(state, userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUserIdAndState(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookingClient.getBookings(userId, state, from, size);
    }


}