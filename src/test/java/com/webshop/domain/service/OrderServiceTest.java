package com.webshop.domain.service;

import com.webshop.domain.model.Order;
import com.webshop.domain.model.Product;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void validateOrder_WithValidOrder_ReturnsTrue() {
        // Arrange
        Order order = new Order();
        Order.OrderItem item = new Order.OrderItem();
        item.setProductId(1L);
        order.setItems(Collections.singletonList(item));

        Product availableProduct = new Product();
        availableProduct.setId(1L);
        availableProduct.setStockQuantity(Quantity.of(10));
        List<Product> availableProducts = Collections.singletonList(availableProduct);

        // Act
        boolean result = orderService.validateOrder(order, availableProducts);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void validateOrder_WithEmptyOrder_ReturnsFalse() {
        // Arrange
        Order order = new Order();
        order.setItems(Collections.emptyList());
        List<Product> availableProducts = Collections.emptyList();

        // Act
        boolean result = orderService.validateOrder(order, availableProducts);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void calculateDiscount_ForVipCustomerAndLargeOrder_AppliesVipDiscount() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Order order = new Order();
        order.setTotalAmount(Money.of(200.00, usd));

        // Act
        Money discount = orderService.calculateDiscount(order, true);

        // Assert
        assertThat(discount.getAmount().doubleValue()).isEqualTo(30.00); // 15% of 200
    }

    @Test
    void calculateDiscount_ForRegularCustomerAndSmallOrder_AppliesNoDiscount() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Order order = new Order();
        order.setTotalAmount(Money.of(50.00, usd));

        // Act
        Money discount = orderService.calculateDiscount(order, false);

        // Assert
        assertThat(discount.getAmount().doubleValue()).isEqualTo(0.0);
    }

    @Test
    void cancelOrder_ForCancellableOrder_ReturnsTrue() {
        // Arrange
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PENDING);

        // Act
        boolean result = orderService.cancelOrder(order);

        // Assert
        assertThat(result).isTrue();
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.CANCELLED);
    }

    @Test
    void cancelOrder_ForNonCancellableOrder_ReturnsFalse() {
        // Arrange
        Order order = new Order();
        order.setStatus(Order.OrderStatus.SHIPPED);

        // Act
        boolean result = orderService.cancelOrder(order);

        // Assert
        assertThat(result).isFalse();
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.SHIPPED);
    }
}