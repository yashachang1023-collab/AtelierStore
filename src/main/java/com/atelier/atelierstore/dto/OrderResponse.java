package com.atelier.atelierstore.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
         Long id,
         String customerEmail,
         String deliveryAddressSnapshot,
         BigDecimal totalAmount,
         String status,
         LocalDateTime createdAt,
         List<OrderItemResponse> items
) {
}