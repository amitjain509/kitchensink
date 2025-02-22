package com.quickstart.kitchensink.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApplicationErrorCode {

    PERMISSION_NOT_EXISTS(1001, "No valid permissions found", HttpStatus.BAD_REQUEST),

    DUPLICATE_ROLE(2001, "Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(2020, "Role does not exist", HttpStatus.NOT_FOUND),
    INVALID_ROLE(2021, "Invalid Role Id", HttpStatus.BAD_REQUEST),
    ROLES_NOT_FOUND(2030, "No valid roles found", HttpStatus.BAD_REQUEST),
    ROLE_ASSOCIATED(2040, "Role is associated with users", HttpStatus.FAILED_DEPENDENCY),

    USER_NOT_FOUND(3001, "User does not exists", HttpStatus.NOT_FOUND),
    USER_EMAIL_EXISTS(3002, "Email already in use", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(4001, "Invalid email or password", HttpStatus.UNAUTHORIZED),
    ;

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    ApplicationErrorCode(int errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
