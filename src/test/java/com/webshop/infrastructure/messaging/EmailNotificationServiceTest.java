package com.webshop.infrastructure.messaging;

import com.webshop.domain.model.Case;
import com.webshop.domain.model.Order;
import com.webshop.domain.model.Return;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.OrderNumber;
import com.webshop.domain.model.vo.Quantity;
import com.webshop.domain.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
class EmailNotificationServiceTest {

    private EmailNotificationService notificationService;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
        notificationService = new EmailNotificationService(customerService);
    }

    @Test
    void sendOrderConfirmation_ValidOrder_ShouldSendEmail() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Order order = new Order();
        order.setOrderNumber(OrderNumber.of("ORD-123"));
        order.setCustomerId(1L);
        Order.OrderItem item = new Order.OrderItem();
        item.setQuantity(Quantity.of(1));
        item.setUnitPrice(Money.of(100.00, usd));
        order.getItems().add(item);

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.sendOrderConfirmation(order));
    }

    @Test
    void sendReturnApproval_ValidReturn_ShouldSendEmail() {
        // Arrange
        Return returnRequest = Return.builder()
                .returnNumber("RET-123")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.sendReturnApproval(returnRequest));
    }

    @Test
    void sendEscalationAlert_ValidCase_ShouldSendEmail() {
        // Arrange
        Case caseEntity = Case.builder()
                .caseNumber("CASE-123")
                .priority(Case.CasePriority.HIGH)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.sendEscalationAlert(caseEntity));
    }

    @Test
    void sendStatusUpdate_ValidMessage_ShouldSendEmail() {
        // Arrange
        Long customerId = 1L;
        String message = "Your order status has been updated";

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.sendStatusUpdate(customerId, message));
    }
}