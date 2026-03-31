package com.atelier.atelierstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String deliveryAddress;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long stationeryId;
        private Integer quantity;
    }
}