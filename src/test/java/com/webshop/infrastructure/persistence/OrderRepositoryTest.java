package com.webshop.infrastructure.persistence;

import com.webshop.domain.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void whenSaveOrder_thenOrderIsPersisted() {
        // Create a test order
        Order order = Order.builder()
                .orderNumber("TEST-001")
                .customerId(1L)
                .totalAmount(new BigDecimal("100.00"))
                .status(Order.OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .shippingAddress("123 Test St")
                .build();

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Verify the order was saved
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderNumber()).isEqualTo("TEST-001");
    }
}