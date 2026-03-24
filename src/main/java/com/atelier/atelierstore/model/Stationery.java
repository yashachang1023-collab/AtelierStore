package com.atelier.atelierstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stationeries")
public class Stationery extends BaseItem{

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 50)
    private String category;

    @NotNull(message = "Stock is required")
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    // Optimistic Locking: Prevents concurrent update issues (e.g., selling more than available)
    // This addresses Interview Question #16
    @Version
    private Long version;

    public Stationery(Long id, String name, BigDecimal price, String category) {
        super(id, name);
        this.price = price;
        this.category = category;
    }

    @Override
    public void displayInfo() {
        System.out.println(String.format(
                "【Stationery Shop】ID: %d | Name: %s | Price: €%s | Category: %s | Stock: %d",
                getId(), getName(), price.toPlainString(), category, stock
        ));
    }
}
