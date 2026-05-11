package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.exception.ErrorCode;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.exception.ResourceNotFoundException;
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
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final StationeryRepository stationeryRepository;

    @Value("${app.finance.vat-rate}")
    private BigDecimal vatRate;

    @Override
    @Transactional(rollbackFor = Exception.class) // Ensures atomicity
    public Order placeOrder(String email, OrderRequest request){
        // 1. Initialize Order with metadata
        Order order = Order.builder()
                .customerEmail(email)
                .deliveryAddressSnapshot(request.getDeliveryAddress())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalVat = BigDecimal.ZERO;

        // 2. Process each item
        for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Stationery stationery = stationeryRepository.findById(itemReq.getStationeryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // Concurrency/Stock check
            if (stationery.getStock() < itemReq.getQuantity()) {
                throw new OutOfStockException(ErrorCode.STOCK_INSUFFICIENT);
            }

            // Deduct stock (Optimistic Locking @Version works here)
            stationery.setStock(stationery.getStock() - itemReq.getQuantity());
            stationeryRepository.save(stationery);

            // 3. Create Item Snapshot
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .stationery(stationery)
                    .quantity(itemReq.getQuantity())
                    .priceAtPurchase(stationery.getPrice())
                    .vatRateAtPurchase(vatRate)
                    .build();

            order.getItems().add(orderItem);

            // Calculations
            BigDecimal itemTotal = stationery.getPrice().multiply(new BigDecimal(itemReq.getQuantity()));
            total = total.add(itemTotal);
            totalVat = totalVat.add(itemTotal.multiply(vatRate));
        }

        order.setTotalAmount(total);
        order.setTotalVatAmount(totalVat);

        // 4. Save entire object graph due to CascadeType.ALL
        return orderRepository.save(order);
    }

}
