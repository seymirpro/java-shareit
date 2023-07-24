package ru.practicum.shareit.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.exception.booking.BookingDoesNotExistException;
import ru.practicum.shareit.exception.booking.BookingStatusBadRequestException;
import ru.practicum.shareit.exception.booking.NotFoundBookingException;
import ru.practicum.shareit.exception.comment.CommentBadRequestException;
import ru.practicum.shareit.exception.item.ItemDoesNotExistException;
import ru.practicum.shareit.exception.item.ItemNotAvailableException;
import ru.practicum.shareit.exception.item.NotItemOwnerException;
import ru.practicum.shareit.exception.user.DuplicateEmailException;
import ru.practicum.shareit.exception.user.UserAlreadyExistsException;
import ru.practicum.shareit.exception.user.UserDoesNotExistException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.user.UserController;

import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice(assignableTypes = {UserController.class, ItemController.class,
        BookingController.class})
public class ErrorHandler {
    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistsException(final UserAlreadyExistsException e) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.getReasonPhrase(),
                String.format(e.getLocalizedMessage())
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
            ConstraintViolationException.class,
            ItemNotAvailableException.class,
            ValidationException.class,
            NotFoundBookingException.class,
            BookingStatusBadRequestException.class,
            CommentBadRequestException.class
    })

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }

    @ExceptionHandler({UserDoesNotExistException.class,
            ItemDoesNotExistException.class,
            NotItemOwnerException.class,
            BookingDoesNotExistException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserDoesNotExistException(final Throwable ex) {
        return new ErrorResponse(
                String.format("Сущность с данным id не существует \"%s\".",
                        ex.getLocalizedMessage())
        );
    }

    @ExceptionHandler({DuplicateEmailException.class,
            IllegalArgumentException.class,
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralExceptions(final RuntimeException ex) {
        String message = "";
        if (ex instanceof DuplicateEmailException) {
            DuplicateEmailException duplicateEmailException = (DuplicateEmailException) ex;
            message = duplicateEmailException.getMessage();
        } else if (ex instanceof IllegalArgumentException) {
            IllegalArgumentException illegalArgumentException = (IllegalArgumentException) ex;
            message = illegalArgumentException.getMessage();
        }

        return new ErrorResponse(
                String.format(message),
                ex.getLocalizedMessage()
        );
    }
}

