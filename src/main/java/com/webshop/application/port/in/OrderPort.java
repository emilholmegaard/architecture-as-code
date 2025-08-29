package com.webshop.application.port.in;

import com.webshop.domain.model.Order;

/**
 * Input port for order operations.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public interface OrderPort {
    Order createOrder(Order order);
    Order updateOrderStatus(Long orderId, Order.OrderStatus status);
}
