package com.quickstart.kitchensink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityAssociationException extends ResponseStatusException {
    public EntityAssociationException(String roleId) {
        super(HttpStatus.FAILED_DEPENDENCY, "Role '" + roleId + "' is associated with users");
    }
}
