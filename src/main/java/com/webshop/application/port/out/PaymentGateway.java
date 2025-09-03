package com.webshop.application.port.out;

import com.webshop.domain.model.vo.Money;

/**
 * Output port for payment processing.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public interface PaymentGateway {
    boolean processPayment(Long customerId, Money amount);

    boolean refundPayment(String transactionId, Money amount);
}
