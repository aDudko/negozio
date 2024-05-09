package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.OrderList;
import net.dudko.project.model.dto.OrderListDto;

public class OrderListMapper {

    public static OrderListDto mapToOrderListDTO(OrderList orderList) {
        return OrderListDto.builder()
                .id(orderList.getId())
                .productId(orderList.getProductId())
                .count(orderList.getCount())
                .build();
    }

    public static OrderList mapToOrderList(OrderListDto orderListDto) {
        return OrderList.builder()
                .id(orderListDto.getId())
                .productId(orderListDto.getProductId())
                .count(orderListDto.getCount())
                .build();
    }

}
