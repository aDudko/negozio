package net.dudko.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.dudko.project.model.dto.OrderDto;
import net.dudko.project.service.AuthService;
import net.dudko.project.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "REST API for Order Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final AuthService authService;

    @Operation(
            summary = "CREATE Order REST API",
            description = "Create Order REST API to save Order into Database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto order, HttpServletRequest request) {
        order.setUserId(authService.getProfile(request).getId());
        orderService.createOrder(order);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

}
