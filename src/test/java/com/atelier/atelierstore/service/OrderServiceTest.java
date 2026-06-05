package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.OrderRequest;
import com.atelier.atelierstore.exception.*;
import com.atelier.atelierstore.model.Order;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.OrderRepository;
import com.atelier.atelierstore.repository.StationeryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StationeryRepository stationeryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(orderService, "vatRate", new BigDecimal("0.19"));
    }

    /**
     * Test successful order placement.
     *
     * This test verifies that:
     * - Order is created successfully when stock is sufficient
     * - Stock is correctly deducted after order placement
     * - Total amount is calculated correctly
     * - VAT amount is calculated correctly
     * - Order is persisted via repository
     *
     * Dependencies (repository layer) are mocked to isolate service logic.
     */
    @Test
    @DisplayName("success")
    void placeOrder_Success(){
        String email = "customer1@123.com";
        Long prodId = 1L;

        Stationery mockStationery = Stationery.builder()
                .id(prodId)
                .stock(100)
                .price(new BigDecimal("10.00"))
                .build();

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest(prodId, 2);

        OrderRequest request = new OrderRequest("berlin", List.of(itemRequest));

        // Stub the repository to return our mock stationery when queried by ID.
        // This ensures the service logic is isolated from the database layer.
        when(stationeryRepository.findById(prodId)).thenReturn(Optional.of(mockStationery));

        // Mock repository save method to return the same Order object passed in.
        // This simulates JPA save behavior without involving the database.
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // --- Act: execute the method under test ---
        Order result = orderService.placeOrder(email, request);

        // --- Assert: verify the result ---
        assertNotNull(result); // Ensure order is created successfully

        // Verify stock is correctly reduced (100 - 2 = 98)
        assertEquals(98, mockStationery.getStock(), "库存应该减少 2 个");

        // Verify total amount calculation (10.00 * 2 = 20.00)
        assertEquals(new BigDecimal("20.00"), result.getTotalAmount(), "总金额计算错误");

        // Verify VAT calculation (20 * 0.19 / 1.19 ≈ 3.1933 → 3.19 after rounding)
        BigDecimal expectedVat = new BigDecimal("3.19");
        assertEquals(0,expectedVat.compareTo(result.getTotalVatAmount().setScale(2, RoundingMode.HALF_UP)));
        // Verify that order was saved once
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    /**
     * Test failed order placement due to insufficient stock.
     *
     * This test verifies that:
     * -  OutOfStockException is thrown when stock is insufficient
     * - StationeryRepository and OrderRepository save methods are not invoked
     */
    @Test
    void placeOrder_Fail_OutOfStock(){
      String email = "Testcustomer2@gmail.com";

      Long proId = 2l;

      Stationery outOfStockStationery = Stationery.builder()
              .id(proId)
              .stock(10)
              .price(new BigDecimal(5.00))
              .build();

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest(proId, 11);
        OrderRequest orderRequest = new OrderRequest("japan", List.of(itemRequest));

        when(stationeryRepository.findById(proId)).thenReturn(Optional.of(outOfStockStationery));

        OutOfStockException ex= assertThrows(OutOfStockException.class, () -> orderService.placeOrder(email, orderRequest));
        assertEquals(ErrorCode.STOCK_INSUFFICIENT, ex.getErrorCode());

        verify(stationeryRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    /**
     * Test failed order placement due to a non-existent product.
     *
     * This test verifies that:
     * -  OutOfStockException is thrown when stock is not be found
     * - StationeryRepository and OrderRepository save methods are not invoked
     */
    @Test
    void placeOrder_Fail_ProductNotFound(){
        String email = "customer3@gamail.com";
        Long productId = 3l;

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest(productId, 1);
        OrderRequest request = new OrderRequest("japan", List.of(itemRequest));

        when(stationeryRepository.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(email, request));

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());

        verify(stationeryRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    /**
     * Test failed order placement due to a invalid customer.
     *
     * This test verifies that:
     * -  Customer is invalid
     * - No methods of StationeryRepository or OrderRepository are invoked
     */
    @Test
    void placeOrder_Fail_InvalidIdentity(){
        long productId = 4l;

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest(productId, 1);
        OrderRequest request = new OrderRequest("japan", List.of(itemRequest));

        InvalidIdentityException ex = assertThrows(InvalidIdentityException.class, () -> orderService.placeOrder(null, request));

        assertEquals(ErrorCode.INVALID_CUSTOMER, ex.getErrorCode());

        verifyNoInteractions(stationeryRepository);
        verifyNoInteractions(orderRepository);

    }

    /**
     * Test failed order placement due to an invalid request.
     *
     * This test verifies that:
     * -  Request is invalid
     * - No methods of StationeryRepository or OrderRepository are invoked
     */
    @Test
    void placeOrder_Fail_InvalidOrder(){
        String email = "customer5@gamil.com";

        InvalidOrderException ex = assertThrows(InvalidOrderException.class, () -> orderService.placeOrder(email, null));

        assertEquals(ErrorCode.EMPTY_ORDER, ex.getErrorCode());

        verifyNoInteractions(stationeryRepository);
        verifyNoInteractions(orderRepository);

    }

}
