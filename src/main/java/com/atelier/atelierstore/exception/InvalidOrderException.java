package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderException extends BusinessException{
    public InvalidOrderException() {
        super(ErrorCode.EMPTY_ORDER, HttpStatus.BAD_REQUEST);
    }
}
