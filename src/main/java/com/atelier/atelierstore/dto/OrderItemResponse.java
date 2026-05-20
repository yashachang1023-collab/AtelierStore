package com.atelier.atelierstore.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a single line item within an order.
 * This DTO is used to send order details back to the frontend.
 */
public record OrderItemResponse (

    // Useful for the frontend to link back to the product page
     Long stationeryId,

    // Flattened field: we take the name from the Stationery entity
    // and put it directly here for easy display.
     String stationeryName,

     String thumbnailUrl,

     Integer quantity,

    // The historical price at the moment of purchase
     BigDecimal priceAtPurchase,

    // German VAT rate snapshot (e.g., 0.19)
     BigDecimal vatRateAtPurchase){
        public BigDecimal getSubTotal() {
            return priceAtPurchase.multiply(new java.math.BigDecimal(quantity));
        }
}