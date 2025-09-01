package com.webshop.application.usecase;

import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Case;
import com.webshop.domain.service.CaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ProcessComplaintUseCaseTest {

    @MockBean
    private CaseService caseService;

    @MockBean
    private NotificationService notificationService;

    private ProcessComplaintUseCase processComplaintUseCase;

    @BeforeEach
    void setUp() {
        processComplaintUseCase = new ProcessComplaintUseCase(caseService, notificationService);
    }

    @Test
    void processComplaint_StandardPriority_NoEscalation() {
        // Arrange
        Case complaint = Case.builder()
                .type(Case.CaseType.COMPLAINT)
                .status(Case.CaseStatus.OPEN)
                .description("Test complaint")
                .build();

        when(caseService.determinePriority(complaint)).thenReturn(Case.CasePriority.MEDIUM);
        when(caseService.escalateIfNeeded(complaint)).thenReturn(false);

        // Act
        Case processedComplaint = processComplaintUseCase.processComplaint(complaint);

        // Assert
        verify(caseService).determinePriority(complaint);
        verify(caseService).escalateIfNeeded(complaint);
        verifyNoInteractions(notificationService);
        assertThat(processedComplaint.getStatus()).isEqualTo(Case.CaseStatus.IN_PROGRESS);
        assertThat(processedComplaint.getPriority()).isEqualTo(Case.CasePriority.MEDIUM);
    }

    @Test
    void processComplaint_HighPriority_WithEscalation() {
        // Arrange
        Case complaint = Case.builder()
                .type(Case.CaseType.COMPLAINT)
                .status(Case.CaseStatus.OPEN)
                .description("Urgent complaint")
                .build();

        when(caseService.determinePriority(complaint)).thenReturn(Case.CasePriority.HIGH);
        when(caseService.escalateIfNeeded(complaint)).thenReturn(true);
        doNothing().when(notificationService).sendEscalationAlert(complaint);

        // Act
        Case processedComplaint = processComplaintUseCase.processComplaint(complaint);

        // Assert
        verify(caseService).determinePriority(complaint);
        verify(caseService).escalateIfNeeded(complaint);
        verify(notificationService).sendEscalationAlert(complaint);
        assertThat(processedComplaint.getStatus()).isEqualTo(Case.CaseStatus.IN_PROGRESS);
        assertThat(processedComplaint.getPriority()).isEqualTo(Case.CasePriority.HIGH);
    }
}