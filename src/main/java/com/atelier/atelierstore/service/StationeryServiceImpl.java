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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyStationery(Long id, Integer num) throws OutOfStockException {
        // 1. Fetch the item
        // .orElseThrow 是 Java 8 Optional 的写法，如果找不到就抛出异常
        Stationery stationery = stationeryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));

        // 2. Check stock levels
        if(stationery.getStock() <= num){
            // Throw custom exception for better API feedback
            throw new OutOfStockException("Insufficient stock for: " + stationery.getName());
        }

        // 3. Update stock
        // Note: JPA will automatically check the @Version field here
        stationery.setStock(stationery.getStock() - num);

        // 4. Save changes
        stationeryRepository.save(stationery);

        // If a network error occurs here, the stock won't be deducted
        // in the database thanks to @Transactional.
    }


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
