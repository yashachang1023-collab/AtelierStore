package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//这个类就像是商店的“投诉中心”。不管店里哪里吵架了（抛出异常），都会被引到这里来解决。
@ControllerAdvice // 告诉 Spring：我是全公司的“投诉中心”
public class GlobalExceptionHandler {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStock(OutOfStockException e){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 你还可以拦截其他所有未预料到的异常（比如空指针）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "服务器开小差了，请稍后再试"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
