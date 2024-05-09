package net.dudko.project.service;

import jakarta.servlet.http.HttpServletRequest;
import net.dudko.project.model.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto, HttpServletRequest request);

}
