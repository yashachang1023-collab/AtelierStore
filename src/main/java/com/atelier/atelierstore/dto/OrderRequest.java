package com.atelier.atelierstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



public record OrderRequest (
         String deliveryAddress,
         List<OrderItemRequest> items
){


    public record OrderItemRequest(
             Long stationeryId,
             Integer quantity) {

    }
}