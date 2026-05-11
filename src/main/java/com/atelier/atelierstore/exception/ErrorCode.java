package com.atelier.atelierstore.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PRODUCT_NOT_FOUND("STATIONERY_001", "The requested stationery product was not found."),
    STOCK_INSUFFICIENT("ORDER_001", "Inventory is insufficient for the requested quantity."),
    ORDER_NOT_FOUND("ORDER_002", "Order not found."),
    // Triggered when Bean Validation (@Valid) fails for incoming DTOs
    VALIDATION_ERROR("VALIDATION_400", "Input validation failed"),
    // --- General System Errors ---

    // Fallback for unexpected exceptions (e.g., NullPointerException, Database connection issues)
    SYSTEM_ERROR("SYS_500", "An unexpected system error occurred. Please try again later.");

    private final String code;
    private final String message;
}
