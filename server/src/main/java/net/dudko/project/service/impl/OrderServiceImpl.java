package net.dudko.project.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.dudko.project.domain.mapper.OrderMapper;
import net.dudko.project.domain.mapper.OrderListMapper;
import net.dudko.project.domain.repository.OrderListRepository;
import net.dudko.project.domain.repository.OrderRepository;
import net.dudko.project.model.dto.OrderDto;
import net.dudko.project.service.AuthService;
import net.dudko.project.service.OrderService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderListRepository orderListRepository;

    private final AuthService authService;

    @Override
    public OrderDto createOrder(OrderDto orderDto, HttpServletRequest request) {
        orderDto.setUserId(authService.getProfile(request).getId());
        for (var orderItem : orderDto.getProducts()) {
            var orderItemId = orderListRepository
                    .save(OrderListMapper.mapToOrderList(orderItem))
                    .getId();
            orderItem.setId(orderItemId);
        }
        var order = orderRepository.save(OrderMapper.mapToOrder(orderDto));
        return OrderMapper.mapToOrderDto(order);
    }

}
