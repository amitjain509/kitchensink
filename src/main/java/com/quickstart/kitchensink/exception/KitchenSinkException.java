package com.quickstart.kitchensink.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class KitchenSinkException extends RuntimeException {
    private final ApplicationErrorCode applicationErrorCode;
    private final String referenceId;
    private final Map<String, Object> errors;
    private final String defaultMessage;

    private KitchenSinkException(ApplicationErrorCode applicationErrorCode, String referenceId, Map<String, Object> errors) {
        super(applicationErrorCode.name());
        this.applicationErrorCode = applicationErrorCode;
        this.referenceId = referenceId;
        this.errors = errors != null ? Collections.unmodifiableMap(errors) : Collections.emptyMap();
        this.defaultMessage = applicationErrorCode.getErrorMessage();
        log.error("Error: {} ReferenceId: {}, ErrorCode: {}", this.defaultMessage, this.referenceId, this.applicationErrorCode);
    }

    public static Builder builder(ApplicationErrorCode errorCode) {
        return new Builder(errorCode);
    }

    public static class Builder {
        private final ApplicationErrorCode errorCode;
        private String referenceId;
        private final Map<String, Object> errors = new HashMap<>();

        private Builder(ApplicationErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder addErrorInformation(String key, Object value) {
            this.errors.put(key, value);
            return this;
        }

        public KitchenSinkException build() {
            return new KitchenSinkException(errorCode, referenceId, errors);
        }
    }
}
