package com.atelier.atelierstore.mapper;

import com.atelier.atelierstore.dto.OrderDTO;
import com.atelier.atelierstore.dto.OrderItemDTO;
import com.atelier.atelierstore.model.Order;
import com.atelier.atelierstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // 1. 将 Order 转换为 OrderDTO
    OrderDTO toDto(Order order);

    // 2. 将 OrderItem 转换为 OrderItemDTO
    // source = "stationery.name" 表示去关联的文具对象里拿 name 属性
    // target = "stationeryName" 对应 DTO 里的字段名
    @Mapping(source = "stationery.id", target = "stationeryId")
    @Mapping(source = "stationery.name", target = "stationeryName")
    OrderItemDTO toDto(OrderItem orderItem);
}
