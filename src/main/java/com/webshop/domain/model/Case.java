package com.webshop.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Case entity for handling customer complaints and issues.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Entity
@Table(name = "cases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caseNumber;
    private Long customerId;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private CaseType type;
    @Enumerated(EnumType.STRING)
    private CaseStatus status;
    private String description;
    private String resolution;
    @Enumerated(EnumType.STRING)
    private CasePriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private String assignedTo;

    public enum CaseType {
        COMPLAINT, RETURN_REQUEST, DAMAGE_CLAIM, GENERAL_INQUIRY, TECHNICAL_ISSUE
    }

    public enum CaseStatus {
        OPEN, IN_PROGRESS, WAITING_FOR_CUSTOMER, ESCALATED, RESOLVED, CLOSED
    }

    public enum CasePriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    /**
     * Checks if the case needs immediate attention
     * 
     * @return true if high priority and open
     */
    public boolean needsImmediateAttention() {
        return (priority == CasePriority.HIGH || priority == CasePriority.CRITICAL)
                && status == CaseStatus.OPEN;
    }

    /**
     * Resolves the case with a resolution message
     * 
     * @param resolution the resolution description
     */
    public void resolve(String resolution) {
        this.resolution = resolution;
        this.status = CaseStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }
}
