package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;

public class InvalidIdentityException extends BusinessException{
    public InvalidIdentityException() {
        super(ErrorCode.INVALID_CUSTOMER, HttpStatus.BAD_REQUEST);
    }
}
