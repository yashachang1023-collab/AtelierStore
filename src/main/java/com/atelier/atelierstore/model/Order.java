package com.atelier.atelierstore.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The unique ID of the buyer (using email from JWT)
    private String customerEmail;

    // Snapshot of address to ensure historical accuracy
    private String deliveryAddressSnapshot;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalVatAmount; // Total tax (MwSt)

    private String status; // e.g., PENDING, PAID, CANCELLED

    private LocalDateTime createdAt;

    // Relationship: One Order has many OrderItems
    // cascade = CascadeType.ALL means if we save the Order, items are saved automatically.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}