package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorResponseTest {
    @Test
    public void testDefaultConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();
        Assertions.assertNotNull(errorResponse);
    }

    @Test
    public void testMessageConstructor() {
        String message = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(message);
        Assertions.assertEquals(message, errorResponse.getMessage());
    }

    @Test
    public void testErrorAndMessageConstructor() {
        String error = "Test error";
        String message = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(error, message);
        Assertions.assertEquals(error, errorResponse.getError());
        Assertions.assertEquals(message, errorResponse.getMessage());
    }

    @Test
    public void testHttpStatusAndMessageConstructor() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, message);
        Assertions.assertEquals(httpStatus.value(), errorResponse.getCode());
        Assertions.assertEquals(httpStatus.name(), errorResponse.getStatus());
        Assertions.assertEquals(message, errorResponse.getMessage());
    }

    @Test
    public void testHttpStatusMessageAndStackTraceConstructor() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Test error message";
        String stackTrace = "Test stack trace";
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, message, stackTrace);
        Assertions.assertEquals(httpStatus.value(), errorResponse.getCode());
        Assertions.assertEquals(httpStatus.name(), errorResponse.getStatus());
        Assertions.assertEquals(message, errorResponse.getMessage());
        Assertions.assertEquals(stackTrace, errorResponse.getStackTrace());
    }

    @Test
    public void testCompleteConstructor() {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String message = "Test error message";
        String stackTrace = "Test stack trace";
        Object data = new Object();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, message, stackTrace, data);
        Assertions.assertEquals(httpStatus.value(), errorResponse.getCode());
        Assertions.assertEquals(httpStatus.name(), errorResponse.getStatus());
        Assertions.assertEquals(message, errorResponse.getMessage());
        Assertions.assertEquals(stackTrace, errorResponse.getStackTrace());
        Assertions.assertEquals(data, errorResponse.getData());
    }

}