package com.atelier.atelierstore.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceNotFoundException(ErrorCode errorCode) {
        // Initialize the base RuntimeException with the enum's message.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
