package com.webshop.infrastructure.messaging;

import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Case;
import com.webshop.domain.model.Order;
import com.webshop.domain.model.Return;
import com.webshop.domain.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * Email-based implementation of NotificationService.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Service
public class EmailNotificationService implements NotificationService {

    private final CustomerService customerService;

    public EmailNotificationService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void sendOrderConfirmation(Order order) {
        // Get customer email from customer service
        String to = customerService.getCustomerEmail(order.getCustomerId())
                .orElse("customer@example.com"); // Fallback for testing
        String subject = "Order Confirmation #" + order.getOrderNumber();
        String content = String.format("Thank you for your order #%s. Your total amount is %s.",
                order.getOrderNumber(), order.getTotalAmount());

        sendEmail(to, subject, content);
    }

    @Override
    public void sendReturnApproval(Return returnRequest) {
        // Simulate sending a return approval email
        String subject = "Return Request Approved #" + returnRequest.getReturnNumber();
        String content = String.format(
                "Your return request #%s has been approved. Please follow the instructions for returning the items.",
                returnRequest.getReturnNumber());

        sendEmail(null, subject, content); // Customer email would be obtained from order or customer service
    }

    @Override
    public void sendEscalationAlert(Case caseEntity) {
        // Simulate sending an escalation alert
        String subject = "Case Escalated #" + caseEntity.getCaseNumber();
        String content = String.format("Case #%s has been escalated to priority %s. Immediate attention required.",
                caseEntity.getCaseNumber(), caseEntity.getPriority());

        sendEmail("support@webshop.com", subject, content);
    }

    @Override
    public void sendStatusUpdate(Long customerId, String message) {
        // Simulate sending a status update
        String subject = "Status Update";
        sendEmail(null, subject, message); // Customer email would be obtained from customer service
    }

    private void sendEmail(String to, String subject, String content) {
        // In a real implementation, this would use JavaMail or similar
        // For testing purposes, we'll just log or simulate sending
        System.out.printf("Sending email to %s: %s - %s%n", to, subject, content);
    }
}