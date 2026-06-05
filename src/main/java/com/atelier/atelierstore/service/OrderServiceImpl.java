package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.dto.OrderResponse;
import com.atelier.atelierstore.exception.*;
import com.atelier.atelierstore.mapper.OrderMapper;
import com.atelier.atelierstore.model.Order;
import com.atelier.atelierstore.model.OrderItem;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.OrderRepository;
import com.atelier.atelierstore.repository.StationeryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final StationeryRepository stationeryRepository;
    private final OrderMapper orderMapper;

    @Value("${app.finance.vat-rate}")
    private BigDecimal vatRate;

    @Override
    @Transactional(rollbackFor = Exception.class) // Ensures atomicity
    public Order placeOrder(String email, OrderRequest request){
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (email == null || email.isBlank()) {
            throw new InvalidIdentityException();
        }

        // 1. Initialize Order with metadata
        Order order = Order.builder()
                .customerEmail(email)
                .deliveryAddressSnapshot(request.deliveryAddress())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalVat = BigDecimal.ZERO;

        // 2. Process each item
        for (OrderRequest.OrderItemRequest itemReq : request.items()) {
            Stationery stationery = stationeryRepository.findById(itemReq.stationeryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // Concurrency/Stock check
            if (stationery.getStock() < itemReq.quantity()) {
                throw new OutOfStockException(ErrorCode.STOCK_INSUFFICIENT);
            }

            // Deduct stock (Optimistic Locking @Version works here)
            stationery.setStock(stationery.getStock() - itemReq.quantity());
            stationeryRepository.save(stationery);

            // 3. Create Item Snapshot
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .stationery(stationery)
                    .quantity(itemReq.quantity())
                    .priceAtPurchase(stationery.getPrice())
                    .vatRateAtPurchase(vatRate)
                    .build();

            order.getItems().add(orderItem);

            // Calculations
            BigDecimal itemTotal = stationery.getPrice().multiply(new BigDecimal(itemReq.quantity()));
            total = total.add(itemTotal);
            // Extract VAT from the tax-inclusive total
            // Formula: VAT = Total - (Total / 1.19)
            BigDecimal divisor = BigDecimal.ONE.add(vatRate); // e.g., 1.19
            // Calculate the Net Amount (tax-exclusive price)
            BigDecimal netAmount = itemTotal.divide(divisor, 4, RoundingMode.HALF_UP);
            // The difference is the VAT portion
            BigDecimal vatAmount = itemTotal.subtract(netAmount);

            totalVat = totalVat.add(vatAmount);
        }

        order.setTotalAmount(total);
        order.setTotalVatAmount(totalVat);

        // 4. Save entire object graph due to CascadeType.ALL
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true) //
    public List<OrderResponse> getOrderHistory(String email) {
        // 1. 调用高性能查询
        List<Order> orders = orderRepository.findHistoryByEmail(email);

        // 2. 转换成 Response 发出去
        return orderMapper.toResponseList(orders);
    }

}
