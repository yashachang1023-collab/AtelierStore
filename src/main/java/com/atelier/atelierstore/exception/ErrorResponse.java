package com.atelier.atelierstore.exception;

import java.time.LocalDateTime;

public record ErrorResponse(int status,
                            String errorCode,
                            String message,
                            LocalDateTime timestamp) {
}