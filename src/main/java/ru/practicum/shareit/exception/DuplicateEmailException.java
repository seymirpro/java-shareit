package ru.practicum.shareit.exception;

import javax.validation.ValidationException;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message){
        super(message);
    }
}
