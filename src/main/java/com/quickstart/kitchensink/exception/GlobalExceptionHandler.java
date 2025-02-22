package com.quickstart.kitchensink.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(KitchenSinkException.class)
    public ResponseEntity<Map<String, Object>> handleKitchenException(KitchenSinkException ex) {
        Map<String, Object> response = getStringObjectMap(ex);

        return ResponseEntity.status(ex.getApplicationErrorCode().getHttpStatus()).body(response);
    }

    private Map<String, Object> getStringObjectMap(KitchenSinkException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getApplicationErrorCode().getErrorMessage());
        response.put("status", ex.getApplicationErrorCode().getHttpStatus().value());
        response.put("referenceId", ex.getReferenceId());
        response.put("errorCode", ex.getApplicationErrorCode().getErrorCode());
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getStringObjectMap(ex.getBindingResult(), HttpStatus.BAD_REQUEST);
    }
    private static Map<String, Object> getStringObjectMap(BindingResult bindingResult, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessages.add(error.getField() + ": " + error.getDefaultMessage());
        }

        response.put("status", httpStatus.value());
        response.put("message", "Validation failed");
        response.put("errors", errorMessages);
        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("message", "Authentication Failed");
        return error;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleAuthenticationException(BadCredentialsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("message", "Authentication Failed");
        return error;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFoundException(UsernameNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("user_error", ex.getMessage());
        return error;
    }
}
