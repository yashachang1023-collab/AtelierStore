package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;

public class OutOfStockException extends BusinessException {

    public OutOfStockException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }
}
