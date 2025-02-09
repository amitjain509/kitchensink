package com.quickstart.kitchensink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {
    public EmailAlreadyExistsException(String email) {
        super(HttpStatus.CONFLICT, "Email '" + email + "' is already in use");
    }
}
