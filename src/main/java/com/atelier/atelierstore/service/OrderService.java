package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.model.Order;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional(rollbackFor = Exception.class) // Ensures atomicity
    Order placeOrder(String email, OrderRequest request);
}
