package com.atelier.atelierstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stationery_id")
    private Stationery stationery;

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceAtPurchase; // Price snapshot

    private BigDecimal vatRateAtPurchase; // Tax snapshot (e.g. 0.19)
}
