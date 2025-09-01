package com.webshop.presentation.rest;

import com.webshop.application.usecase.ProcessOrderUseCase;
import com.webshop.domain.model.Order;
import com.webshop.presentation.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ProcessOrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = convertToEntity(orderDto);
        Order createdOrder = orderUseCase.createOrder(order);
        return ResponseEntity.ok(convertToDto(createdOrder));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        Order updatedOrder = orderUseCase.updateOrderStatus(orderId, Order.OrderStatus.valueOf(status));
        return ResponseEntity.ok(convertToDto(updatedOrder));
    }

    private Order convertToEntity(OrderDto dto) {
        // Implement conversion logic
        return new Order();
    }

    private OrderDto convertToDto(Order order) {
        // Implement conversion logic
        return new OrderDto();
    }
}