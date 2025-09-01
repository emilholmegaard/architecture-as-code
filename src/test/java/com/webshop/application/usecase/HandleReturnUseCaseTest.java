package com.webshop.application.usecase;

import com.webshop.application.port.out.NotificationService;
import com.webshop.domain.model.Case;
import com.webshop.domain.model.Return;
import com.webshop.domain.service.CaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class HandleReturnUseCaseTest {

    @MockBean
    private CaseService caseService;

    @MockBean
    private NotificationService notificationService;

    private HandleReturnUseCase handleReturnUseCase;

    @BeforeEach
    void setUp() {
        handleReturnUseCase = new HandleReturnUseCase(caseService, notificationService);
    }

    @Test
    void handleReturn_ValidRequest_Success() {
        // Arrange
        Return returnRequest = new Return();
        Case associatedCase = Case.builder()
                .type(Case.CaseType.RETURN_REQUEST)
                .status(Case.CaseStatus.OPEN)
                .build();

        when(caseService.validateReturnRequest(eq(returnRequest), any())).thenReturn(true);
        doNothing().when(notificationService).sendReturnApproval(returnRequest);

        // Act
        Return processedReturn = handleReturnUseCase.handleReturn(returnRequest, associatedCase);

        // Assert
        verify(notificationService).sendReturnApproval(returnRequest);
        assertThat(processedReturn).isEqualTo(returnRequest);
        assertThat(associatedCase.getStatus()).isEqualTo(Case.CaseStatus.IN_PROGRESS);
    }

    @Test
    void handleReturn_InvalidRequest_Rejected() {
        // Arrange
        Return returnRequest = new Return();
        Case associatedCase = Case.builder()
                .type(Case.CaseType.RETURN_REQUEST)
                .status(Case.CaseStatus.OPEN)
                .build();

        when(caseService.validateReturnRequest(eq(returnRequest), any())).thenReturn(false);

        // Act
        Return processedReturn = handleReturnUseCase.handleReturn(returnRequest, associatedCase);

        // Assert
        verify(notificationService, never()).sendReturnApproval(any());
        assertThat(processedReturn).isEqualTo(returnRequest);
        assertThat(associatedCase.getStatus()).isEqualTo(Case.CaseStatus.OPEN);
    }
}