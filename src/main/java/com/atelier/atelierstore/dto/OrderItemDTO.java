package com.atelier.atelierstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Data Transfer Object representing a single line item within an order.
 * This DTO is used to send order details back to the frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    // Useful for the frontend to link back to the product page
    private Long stationeryId;

    // Flattened field: we take the name from the Stationery entity
    // and put it directly here for easy display.
    private String stationeryName;

    private Integer quantity;

    // The historical price at the moment of purchase
    private BigDecimal priceAtPurchase;

    // German VAT rate snapshot (e.g., 0.19)
    private BigDecimal vatRateAtPurchase;
}