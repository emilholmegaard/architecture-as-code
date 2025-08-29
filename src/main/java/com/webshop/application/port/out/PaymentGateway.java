package com.webshop.application.port.out;

import java.math.BigDecimal;

/**
 * Output port for payment processing.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public interface PaymentGateway {
    boolean processPayment(Long customerId, BigDecimal amount);
    boolean refundPayment(String transactionId, BigDecimal amount);
}
