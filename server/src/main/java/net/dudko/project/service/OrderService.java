package net.dudko.project.service;

import net.dudko.project.model.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

}
