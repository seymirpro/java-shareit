package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateTest {
    @Test
    public void testEnumValues() {
        BookingState[] states = BookingState.values();
        Assertions.assertEquals(6, states.length);

        Assertions.assertArrayEquals(new BookingState[]{BookingState.ALL, BookingState.CURRENT, BookingState.PAST,
                BookingState.FUTURE, BookingState.WAITING, BookingState.REJECTED}, states);
    }
}