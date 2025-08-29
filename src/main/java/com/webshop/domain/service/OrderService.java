package com.webshop.domain.service;

import com.webshop.domain.model.Order;
import com.webshop.domain.model.Product;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * Domain service for order business logic.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Service
public class OrderService {
    
    /**
     * Validates if an order can be placed
     * @param order the order to validate
     * @param availableProducts available products
     * @return true if order is valid
     */
    public boolean validateOrder(Order order, List<Product> availableProducts) {
        if (order.getItems().isEmpty()) {
            return false;
        }
        
        return order.getItems().stream().allMatch(item -> 
            availableProducts.stream()
                .anyMatch(p -> p.getId().equals(item.getProductId()) && p.isAvailable())
        );
    }
    
    /**
     * Calculates discount based on customer type and order amount
     * @param order the order
     * @param isVip whether customer is VIP
     * @return discount amount
     */
    public BigDecimal calculateDiscount(Order order, boolean isVip) {
        BigDecimal total = order.calculateTotal();
        BigDecimal discountRate = isVip ? new BigDecimal("0.15") : new BigDecimal("0.05");
        
        if (total.compareTo(new BigDecimal("100")) > 0) {
            return total.multiply(discountRate);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Processes order cancellation
     * @param order the order to cancel
     * @return true if cancellation successful
     */
    public boolean cancelOrder(Order order) {
        if (!order.isCancellable()) {
            return false;
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        return true;
    }
}
