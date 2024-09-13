package com.example.onlinebookstore.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
    private static final String NPE_MESSAGE = "Null pointer exception occurred";
    private static final String ALL_EXCEPTION_MESSAGE = "An unexpected error occurred";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(
            NullPointerException ex) {

        return new ResponseEntity<>(NPE_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception ex) {

        return new ResponseEntity<>(ALL_EXCEPTION_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ObjectError e) {
        String defaultMessage = e.getDefaultMessage();
        return (e instanceof FieldError fieldError) ? fieldError.getField() + " " + defaultMessage
                : defaultMessage;
    }
}
