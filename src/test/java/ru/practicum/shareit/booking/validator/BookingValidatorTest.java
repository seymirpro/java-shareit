package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingValidatorTest {

    @Test
    public void testValidStartEndDt() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        Assertions.assertDoesNotThrow(() -> BookingValidator.startEndDtValidation(start, end));
    }

    @Test
    public void testInvalidStartEndDt() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        Assertions.assertThrows(ValidationException.class, () -> BookingValidator.startEndDtValidation(start, end));
    }

    @Test
    public void testStartBeforeCurrentDt() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        Assertions.assertThrows(ValidationException.class, () -> BookingValidator.startEndDtValidation(start, end));
    }
}