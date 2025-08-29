package com.webshop.application.usecase;

import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Case;
import com.webshop.domain.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for processing customer complaints.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ProcessComplaintUseCase {
    
    private final CaseService caseService;
    private final NotificationService notificationService;
    
    /**
     * Processes a customer complaint
     * @param complaint the complaint case
     * @return processed complaint
     */
    @Transactional
    public Case processComplaint(Case complaint) {
        // Set priority
        Case.CasePriority priority = caseService.determinePriority(complaint);
        complaint.setPriority(priority);
        
        // Check if escalation needed
        if (caseService.escalateIfNeeded(complaint)) {
            notificationService.sendEscalationAlert(complaint);
        }
        
        // Update status
        complaint.setStatus(Case.CaseStatus.IN_PROGRESS);
        
        return complaint;
    }
}
