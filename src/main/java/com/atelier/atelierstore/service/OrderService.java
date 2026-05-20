package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.dto.OrderResponse;
import com.atelier.atelierstore.model.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    Order placeOrder(String email, OrderRequest request);

    List<OrderResponse> getOrderHistory(String email);
}
