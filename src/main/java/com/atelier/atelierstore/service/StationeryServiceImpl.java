package com.atelier.atelierstore.service;

import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.StationeryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationeryServiceImpl implements StationeryService{
    private final StationeryRepository stationeryRepository;
    /**
     * Executes the purchase logic.
     * @Transactional ensures that if any part fails, the entire process rolls back.
     */


    // Search for stationery by category
    @Override
    public List<Stationery> getStationeryByCategory(String category) {
        return stationeryRepository.findByCategoryContainingIgnoreCase(category);
    }

    //Get all stationery
    @Override
    public List<Stationery> getAllStationery() {
        return stationeryRepository.findAll();
    }
}
