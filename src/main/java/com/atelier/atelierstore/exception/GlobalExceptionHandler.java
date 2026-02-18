package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

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

    /**
     * Handles validation exceptions triggered by the @Valid annotation.
     * This method intercepts MethodArgumentNotValidException and converts it
     * into a readable format for the frontend.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e){
        //  Step 1. Extract field errors from the exception's BindingResult.
        //  e.getBindingResult().getFieldErrors(). returns a list of all fields that failed validation.
        String errorMessage = e.getBindingResult().getFieldErrors().stream()// Start Stream processing

                // Step 2: [Mapping] Transform complex FieldError objects into simple strings.
                // We combine the field name and the default message (defined in the DTO).
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                // Step 3: [Collecting] Join all individual error messages into a single string.
                // Semicolons are used as separators (e.g., "name: is required; imageUrl: must be a valid URL").
                .collect(Collectors.joining("; "));

        // Step 4: Encapsulation.
        // Use the custom ErrorResponse class to ensure a consistent error structure across the entire API.
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

        // Step 5: Return the response with an explicit HTTP 400 (Bad Request) status.
        // 400 is the standard status for client-side input errors.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
