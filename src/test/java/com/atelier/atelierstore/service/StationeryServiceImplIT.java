package com.atelier.atelierstore.service;

import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.StationeryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StationeryServiceImplIT {

    @Autowired
    private StationeryRepository stationeryRepository;

    @Autowired
    private StationeryService stationeryService;

    @Test
    public void testOptimisticLocking_ShouldThrowException(){
        //1. Arrange: Create and save an initial stationery item
        Stationery stationery = new Stationery();
        stationery.setName("Limited Edition Pen");
        stationery.setPrice(new BigDecimal("99.99"));
        stationery.setStock(10);
        stationery.setCategory("Premium");

        Stationery savedItem = stationeryRepository.save(stationery);
        Long stationeryId = savedItem.getId();

        // 2. Simulate two users fetching the same record at the same time
        // Both users now have the object with version = 0
        Stationery userA_view = stationeryRepository.findById(stationeryId).get();
        Stationery userB_view = stationeryRepository.findById(stationeryId).get();

        // 3. User A updates the stock and saves
        userA_view.setStock(9);
        stationeryRepository.save(userA_view); // Database version becomes 1

        // 4. User B tries to update the same record using their stale data (version 0)
        userB_view.setStock(5);

        // 5. Assert: Saving user B's view should throw an Optimistic Locking exception
        // because the version in the database (1) no longer matches user B's version (0)
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            stationeryRepository.save(userB_view);
        });
    }
}
