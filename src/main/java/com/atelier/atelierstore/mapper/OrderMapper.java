package com.atelier.atelierstore.mapper;

import com.atelier.atelierstore.dto.OrderResponse;
import com.atelier.atelierstore.dto.OrderItemResponse;
import com.atelier.atelierstore.model.Order;
import com.atelier.atelierstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // 1. 将 Order 转换为 OrderResponse
    OrderResponse toDto(Order order);
    List<OrderResponse> toResponseList(List<Order> orders);

    // 2. 将 OrderItem 转换为 OrderItemResponse
    // source = "stationery.name" 表示去关联的文具对象里拿 name 属性
    // target = "stationeryName" 对应 DTO 里的字段名
    @Mapping(source = "stationery.id", target = "stationeryId")
    @Mapping(source = "stationery.name", target = "stationeryName")
    @Mapping(source = "stationery.imageUrl", target = "thumbnailUrl")
    OrderItemResponse toDto(OrderItem orderItem);
}
