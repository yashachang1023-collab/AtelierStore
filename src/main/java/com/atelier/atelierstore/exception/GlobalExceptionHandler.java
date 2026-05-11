package com.atelier.atelierstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

//这个类就像是商店的“投诉中心”。不管店里哪里吵架了（抛出异常），都会被引到这里来解决。
@ControllerAdvice // 告诉 Spring：我是全公司的“投诉中心”
public class GlobalExceptionHandler {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStock(OutOfStockException e){
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),errorCode.getCode(), errorCode.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for all uncaught or unexpected exceptions (e.g., NullPointerException).
     * Ensures the API always returns a consistent JSON structure instead of a raw stack trace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        e.printStackTrace();

        ErrorCode errorCode = ErrorCode.SYSTEM_ERROR;
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now()
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
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ErrorCode.VALIDATION_ERROR.getCode(),
                errorMessage,
                LocalDateTime.now());

        // Step 5: Return the response with an explicit HTTP 400 (Bad Request) status.
        // 400 is the standard status for client-side input errors.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
