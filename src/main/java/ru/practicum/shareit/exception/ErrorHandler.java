package ru.practicum.shareit.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.user.UserController;

@RestControllerAdvice(assignableTypes = {UserController.class, ItemController.class})
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
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }

    @ExceptionHandler({UserDoesNotExistException.class, ItemDoesNotExistException.class,
            NotItemOwnerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserDoesNotExistException(final RuntimeException ex) {
        return new ErrorResponse(
                String.format("Пользователь с данным id не существует \"%s\".",
                        ex.getLocalizedMessage())
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralExceptions(final DuplicateEmailException ex) {
        return new ErrorResponse(
                String.format(ex.getLocalizedMessage())
        );
    }
}

