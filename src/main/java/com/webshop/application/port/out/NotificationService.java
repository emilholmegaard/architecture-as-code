package com.webshop.application.port.out;

import com.webshop.domain.model.Order;
import com.webshop.domain.model.Case;
import com.webshop.domain.model.Return;

/**
 * Output port for notification services.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public interface NotificationService {
    void sendOrderConfirmation(Order order);
    void sendReturnApproval(Return returnRequest);
    void sendEscalationAlert(Case caseEntity);
    void sendStatusUpdate(Long customerId, String message);
}
