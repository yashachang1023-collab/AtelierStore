package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Fetches user order history with optimized JOIN FETCH.
     * Prevents N+1 queries by pre-loading OrderItems and Stationery details
     * in one single database hit.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.stationery s " +
            "WHERE o.customerEmail = :email " +
            "ORDER BY o.createdAt DESC")
    List<Order> findHistoryByEmail(@Param("email") String email);
    //List<Order> findByCustomerEmail(String email);
}
