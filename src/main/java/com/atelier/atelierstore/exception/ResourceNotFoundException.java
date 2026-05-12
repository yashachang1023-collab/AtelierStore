package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(ErrorCode errorCode) {
            super(errorCode, HttpStatus.NOT_FOUND);
    }
}
