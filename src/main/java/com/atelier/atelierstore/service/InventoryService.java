package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Stationery;

import java.util.List;

public interface InventoryService {
    List<IllustrationDTO> getAllIllustration();
    void addIllustration(IllustrationDTO illustrationDTO);
    void deleteIllustration(Long id);
    void buyStationery(String id, Integer num) throws OutOfStockException;
    List<Stationery> getStationeryByCategory(String category);
}
