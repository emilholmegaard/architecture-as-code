package com.webshop.application.usecase;

import com.webshop.application.port.in.OrderPort;
import com.webshop.application.port.out.PaymentGateway;
import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Order;
import com.webshop.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for processing customer orders.
 * Orchestrates the order processing workflow.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ProcessOrderUseCase implements OrderPort {

    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;

    /**
     * Processes a new order through the complete workflow
     * 
     * @param order the order to process
     * @return processed order
     */
    @Transactional
    public Order processOrder(Order order) {
        // Validate order
        if (!orderService.validateOrder(order, null)) {
            throw new IllegalArgumentException("Invalid order");
        }

        // Process payment
        boolean paymentSuccess = paymentGateway.processPayment(
                order.getCustomerId(),
                order.getTotalAmount());

        if (!paymentSuccess) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            throw new RuntimeException("Payment failed");
        }

        // Update order status
        order.setStatus(Order.OrderStatus.CONFIRMED);

        // Send confirmation
        notificationService.sendOrderConfirmation(order);

        return order;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        return processOrder(order);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        // Implementation for updating order status
        return null;
    }
}
