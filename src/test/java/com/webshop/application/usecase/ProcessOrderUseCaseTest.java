package com.webshop.application.usecase;

import com.webshop.application.port.out.NotificationService;
import com.webshop.application.port.out.PaymentGateway;
import com.webshop.domain.model.Order;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProcessOrderUseCaseTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentGateway paymentGateway;

    @MockBean
    private NotificationService notificationService;

    private ProcessOrderUseCase processOrderUseCase;

    @BeforeEach
    void setUp() {
        processOrderUseCase = new ProcessOrderUseCase(orderService, paymentGateway, notificationService);
    }

    @Test
    void createOrder_ValidOrder_Success() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Order order = new Order();
        order.setCustomerId(1L);
        order.setTotalAmount(Money.of(100.00, usd));

        when(orderService.validateOrder(any(), any())).thenReturn(true);
        when(paymentGateway.processPayment(any(), any())).thenReturn(true);
        doNothing().when(notificationService).sendOrderConfirmation(order);

        // Act
        Order result = processOrderUseCase.createOrder(order);

        // Assert
        verify(orderService).validateOrder(any(), any());
        verify(paymentGateway).processPayment(any(), any());
        verify(notificationService).sendOrderConfirmation(order);
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.CONFIRMED);
    }

    @Test
    void createOrder_PaymentFailed_ThrowsException() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Order order = new Order();
        order.setCustomerId(1L);
        order.setTotalAmount(Money.of(100.00, usd));

        when(orderService.validateOrder(any(), any())).thenReturn(true);
        when(paymentGateway.processPayment(any(), any())).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            processOrderUseCase.createOrder(order);
        });

        verify(orderService).validateOrder(any(), any());
        verify(paymentGateway).processPayment(any(), any());
        verifyNoInteractions(notificationService);
        assertThat(exception)
                .hasMessage("Payment failed");
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.CANCELLED);
    }

    @Test
    void updateOrderStatus_ValidStatus_Success() {
        // Arrange
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.SHIPPED;

        // Act
        processOrderUseCase.updateOrderStatus(orderId, newStatus);

        // Assert - Currently no implementation to verify
        // This test should be updated once updateOrderStatus is implemented
        verifyNoInteractions(orderService, paymentGateway, notificationService);
    }
}