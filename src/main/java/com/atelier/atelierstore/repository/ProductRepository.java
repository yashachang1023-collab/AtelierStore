package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.BaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 考点：JpaRepository
 * 你不需要写任何 SQL，它自带了 save(), findById(), findAll() 等所有功能。
 */
public interface ProductRepository extends JpaRepository<BaseItem, String> {
}
