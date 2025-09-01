package com.webshop.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Return entity for handling product returns.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Entity
@Table(name = "returns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String returnNumber;
    private Long orderId;
    private Long caseId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnItem> items;
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    @Enumerated(EnumType.STRING)
    private ReturnReason reason;
    private BigDecimal refundAmount;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private String notes;

    public enum ReturnStatus {
        REQUESTED, APPROVED, REJECTED, SHIPPED_BACK, RECEIVED, REFUNDED, COMPLETED
    }

    public enum ReturnReason {
        DEFECTIVE, WRONG_ITEM, NOT_AS_DESCRIBED, DAMAGED, CHANGED_MIND, TOO_LATE
    }

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long productId;
        private Integer quantity;
        private BigDecimal refundAmount;
        private String condition;
    }

    /**
     * Checks if return is within valid return window (30 days)
     * 
     * @param orderDate the original order date
     * @return true if within return window
     */
    public boolean isWithinReturnWindow(LocalDateTime orderDate) {
        return requestDate.isBefore(orderDate.plusDays(30));
    }

    /**
     * Approves the return request
     */
    public void approve() {
        this.status = ReturnStatus.APPROVED;
    }

    /**
     * Rejects the return request
     * 
     * @param reason reason for rejection
     */
    public void reject(String reason) {
        this.status = ReturnStatus.REJECTED;
        this.notes = reason;
    }
}
