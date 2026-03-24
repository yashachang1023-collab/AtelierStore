package com.atelier.atelierstore.service;

import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.StationeryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationeryServiceImplTest {
    @Mock
    private StationeryRepository stationeryRepository;

    @InjectMocks
    private StationeryServiceImpl stationeryServiceImpl;

    @Test
    public void testBuyStationery_ThrowsExceptionWhenStockLow(){
        Stationery mockPen = new Stationery();
        mockPen.setName("Test Pen");
        mockPen.setStock(5);

        when(stationeryRepository.findById(1L)).thenReturn(Optional.of(mockPen));

        // Act & Assert (执行并断言)
        // 我们预期：买 10 个（超过库存）一定会抛出 OutOfStockException
        assertThrows(OutOfStockException.class, () -> {
            stationeryServiceImpl.buyStationery(1L, 10);
        });

    }
}
