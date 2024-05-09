package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.Order;
import net.dudko.project.model.dto.OrderDto;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .products(order.getOrderList().stream()
                        .map(OrderListMapper::mapToOrderListDTO)
                        .toList())
                .build();

    }

    public static Order mapToOrder(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .userId(orderDto.getUserId())
                .orderList(orderDto.getProducts().stream()
                        .map(OrderListMapper::mapToOrderList)
                        .toList())
                .build();
    }

}
