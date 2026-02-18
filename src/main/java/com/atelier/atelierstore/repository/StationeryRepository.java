package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.Stationery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationeryRepository extends JpaRepository<Stationery, String> {
    // 考点：Query Method 命名规范
    // 只要你叫这个名字，Spring 就会自动生成 SQL: SELECT * FROM stationery WHERE category = ?
    List<Stationery> findByCategoryContainingIgnoreCase(String category);
}
