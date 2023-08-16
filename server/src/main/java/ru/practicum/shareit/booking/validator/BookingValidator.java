package ru.practicum.shareit.booking.validator;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

public final class BookingValidator {
    public static void startEndDtValidation(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) ||
                start.equals(end) ||
                start.isBefore(LocalDateTime.now()) ||
                !end.isAfter(start)) {
            throw new ValidationException();
        }
    }
}
