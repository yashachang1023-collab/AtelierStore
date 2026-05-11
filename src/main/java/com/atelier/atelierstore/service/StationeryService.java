package com.atelier.atelierstore.service;

import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Stationery;

import java.util.List;

public interface StationeryService {
    List<Stationery> getStationeryByCategory(String category);
    List<Stationery> getAllStationery();
}
