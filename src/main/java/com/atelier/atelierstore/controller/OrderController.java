package com.atelier.atelierstore.controller;

import com.atelier.atelierstore.dto.OrderResponse;
import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.mapper.OrderMapper;
import com.atelier.atelierstore.model.Order;
import com.atelier.atelierstore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    /**
     * Endpoint to place a new order.
     * The @Valid annotation triggers bean validation for the OrderRequest.
     * The Authentication object is automatically injected by Spring Security.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) throws OutOfStockException {

        // 1. Extract the current user's email from the JWT token.
        // This is much more secure than getting it from the request body.
        String customerEmail = authentication.getName();

        // 2. Execute the transactional business logic
        Order savedOrder = orderService.placeOrder(customerEmail, request);

        // 3. Map the saved entity to a DTO and return it with 200 OK
        return ResponseEntity.ok(orderMapper.toDto(savedOrder));
    }


    /**
     * Retrieves the order history for the currently authenticated user.
     */
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getMyOrderHistory(Authentication authentication) {
        // Retrieve the current user's email from the verified JWT principal
        String currentUserEmail = authentication.getName();

        // Call the service to fetch optimized order data
        List<OrderResponse> history = orderService.getOrderHistory(currentUserEmail);

        // Return the response with 200 OK status
        return ResponseEntity.ok(history);
    }
}