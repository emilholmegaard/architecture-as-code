package com.webshop.infrastructure.persistence;

import com.webshop.domain.model.Order;
import com.webshop.domain.model.vo.Address;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.OrderNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Currency;

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
        Currency usd = Currency.getInstance("USD");
        Order order = Order.builder()
                .orderNumber(OrderNumber.of("TEST-001"))
                .customerId(1L)
                .totalAmount(Money.of(100.00, usd))
                .status(Order.OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .shippingAddress(Address.of("123 Test St", "Testville", "12345", "USA"))
                .build();

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Verify the order was saved
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderNumber().getValue()).isEqualTo("TEST-001");
    }
}