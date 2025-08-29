package com.webshop.application.usecase;

import com.webshop.application.port.in.CaseHandlingPort;
import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Case;
import com.webshop.domain.model.Return;
import com.webshop.domain.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for handling product returns.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class HandleReturnUseCase {
    
    private final CaseService caseService;
    private final NotificationService notificationService;
    
    /**
     * Handles a return request
     * @param returnRequest the return request
     * @param associatedCase the associated case
     * @return processed return
     */
    @Transactional
    public Return handleReturn(Return returnRequest, Case associatedCase) {
        // Validate return request
        if (!caseService.validateReturnRequest(returnRequest, null)) {
            returnRequest.reject("Return window expired or order not delivered");
            return returnRequest;
        }
        
        // Approve return
        returnRequest.approve();
        
        // Update case
        associatedCase.setStatus(Case.CaseStatus.IN_PROGRESS);
        
        // Send notification
        notificationService.sendReturnApproval(returnRequest);
        
        return returnRequest;
    }
}
