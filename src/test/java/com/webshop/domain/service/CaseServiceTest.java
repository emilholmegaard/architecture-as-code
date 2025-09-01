package com.webshop.domain.service;

import com.webshop.domain.model.Case;
import com.webshop.domain.model.Order;
import com.webshop.domain.model.Return;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class CaseServiceTest {

    private CaseService caseService;

    @BeforeEach
    void setUp() {
        caseService = new CaseService();
    }

    @Test
    void determinePriority_DamageClaim_ReturnsHigh() {
        // Arrange
        Case caseEntity = Case.builder()
                .type(Case.CaseType.DAMAGE_CLAIM)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        Case.CasePriority priority = caseService.determinePriority(caseEntity);

        // Assert
        assertThat(priority).isEqualTo(Case.CasePriority.HIGH);
    }

    @Test
    void determinePriority_OldComplaint_ReturnsHigh() {
        // Arrange
        Case caseEntity = Case.builder()
                .type(Case.CaseType.COMPLAINT)
                .createdAt(LocalDateTime.now().minusHours(49))
                .build();

        // Act
        Case.CasePriority priority = caseService.determinePriority(caseEntity);

        // Assert
        assertThat(priority).isEqualTo(Case.CasePriority.HIGH);
    }

    @Test
    void determinePriority_RecentComplaint_ReturnsMedium() {
        // Arrange
        Case caseEntity = Case.builder()
                .type(Case.CaseType.COMPLAINT)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        // Act
        Case.CasePriority priority = caseService.determinePriority(caseEntity);

        // Assert
        assertThat(priority).isEqualTo(Case.CasePriority.MEDIUM);
    }

    @Test
    void validateReturnRequest_ValidReturn_ReturnsTrue() {
        // Arrange
        Return returnRequest = Return.builder()
                .requestDate(LocalDateTime.now())
                .build();
        Order order = new Order();
        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setOrderDate(LocalDateTime.now().minusDays(10));

        // Act
        boolean result = caseService.validateReturnRequest(returnRequest, order);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void validateReturnRequest_UndeliveredOrder_ReturnsFalse() {
        // Arrange
        Return returnRequest = new Return();
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PROCESSING);
        order.setOrderDate(LocalDateTime.now().minusDays(1));

        // Act
        boolean result = caseService.validateReturnRequest(returnRequest, order);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void escalateIfNeeded_CriticalCase_ReturnsTrue() {
        // Arrange
        Case caseEntity = Case.builder()
                .status(Case.CaseStatus.OPEN)
                .priority(Case.CasePriority.CRITICAL)
                .build();

        // Act
        boolean result = caseService.escalateIfNeeded(caseEntity);

        // Assert
        assertThat(result).isTrue();
        assertThat(caseEntity.getStatus()).isEqualTo(Case.CaseStatus.ESCALATED);
    }

    @Test
    void escalateIfNeeded_NonCriticalCase_ReturnsFalse() {
        // Arrange
        Case caseEntity = Case.builder()
                .status(Case.CaseStatus.OPEN)
                .priority(Case.CasePriority.MEDIUM)
                .build();

        // Act
        boolean result = caseService.escalateIfNeeded(caseEntity);

        // Assert
        assertThat(result).isFalse();
        assertThat(caseEntity.getStatus()).isEqualTo(Case.CaseStatus.OPEN);
    }
}