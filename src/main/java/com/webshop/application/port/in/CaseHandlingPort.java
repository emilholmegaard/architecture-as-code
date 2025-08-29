package com.webshop.application.port.in;

import com.webshop.domain.model.Case;
import com.webshop.domain.model.Return;

/**
 * Input port for case handling operations.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public interface CaseHandlingPort {
    Case createCase(Case caseEntity);
    Return processReturn(Return returnRequest);
    Case resolveCase(Long caseId, String resolution);
}
