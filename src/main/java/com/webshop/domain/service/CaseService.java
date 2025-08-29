package com.webshop.domain.service;

import com.webshop.domain.model.Case;
import com.webshop.domain.model.Return;
import com.webshop.domain.model.Order;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Domain service for case handling business logic.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Service
public class CaseService {
    
    /**
     * Determines priority based on case type and age
     * @param caseEntity the case
     * @return calculated priority
     */
    public Case.CasePriority determinePriority(Case caseEntity) {
        long hoursSinceCreation = ChronoUnit.HOURS.between(caseEntity.getCreatedAt(), LocalDateTime.now());
        
        if (caseEntity.getType() == Case.CaseType.DAMAGE_CLAIM || hoursSinceCreation > 48) {
            return Case.CasePriority.HIGH;
        } else if (caseEntity.getType() == Case.CaseType.COMPLAINT || hoursSinceCreation > 24) {
            return Case.CasePriority.MEDIUM;
        }
        return Case.CasePriority.LOW;
    }
    
    /**
     * Validates return request
     * @param returnRequest the return request
     * @param order the original order
     * @return true if return is valid
     */
    public boolean validateReturnRequest(Return returnRequest, Order order) {
        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            return false;
        }
        
        return returnRequest.isWithinReturnWindow(order.getOrderDate());
    }
    
    /**
     * Escalates case if needed based on rules
     * @param caseEntity the case
     * @return true if escalated
     */
    public boolean escalateIfNeeded(Case caseEntity) {
        if (caseEntity.getStatus() == Case.CaseStatus.OPEN && 
            caseEntity.getPriority() == Case.CasePriority.CRITICAL) {
            caseEntity.setStatus(Case.CaseStatus.ESCALATED);
            return true;
        }
        return false;
    }
}
