package com.quickstart.kitchensink.exception;

import com.mongodb.DuplicateKeyException;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final List<String> errorMessages = new ArrayList<>();

    @ExceptionHandler({DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleExceptions(RuntimeException ex) {
        return getStringObjectMap(ex);
    }

    @ExceptionHandler({DuplicateRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDuplicateRequestException(RuntimeException ex) {
        return getStringObjectMap(ex);
    }

    private Map<String, Object> getStringObjectMap(RuntimeException ex) {
        errorMessages.add(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("messages", errorMessages);

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailConflict(EmailAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("email", ex.getReason());
        return error;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("authentication_error", ex.getMessage());
        return error;
    }
}
