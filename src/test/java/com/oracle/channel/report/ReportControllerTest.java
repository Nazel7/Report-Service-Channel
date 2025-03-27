package com.oracle.channel.report;

import com.oracle.channel.report.controllers.ReportController;
import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ReportController} class.
 * This class tests the controller methods responsible for handling report operations.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
class ReportControllerTest {

    /**
     * The controller instance under test.
     */
    @InjectMocks
    private ReportController reportController;

    /**
     * Mocked instance of {@link ReportService} to simulate service layer operations.
     */
    @Mock
    private ReportService reportService;

    /**
     * Sets up test environment before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code saveReport} method to ensure successful report saving.
     * Validates response status and returned object.
     */
    @Test
    @DisplayName("test_save_report_return_success_with_parameter_assertion_n_verification")
    void testSaveReport() {
        // Create a mock request DTO
        ReportRequestDto requestDto = new ReportRequestDto();
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", requestDto);

        // Mock the service layer response
        when(reportService.saveReport(any(ReportRequestDto.class))).thenReturn(expectedResponse);

        // Call the controller method
        ResponseEntity<GlobalResponse> response = reportController.saveReport(requestDto);

        // Assert that the response status is 200 (OK)
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());

        // Verify that the service method was called exactly once
        verify(reportService, times(1)).saveReport(any(ReportRequestDto.class));
    }

    /**
     * Tests the {@code deleteReport} method to ensure successful report deletion.
     * Checks that the service method is called and the response is valid.
     */
    @Test
    void testDeleteReport() {
        final long batchNo = 12345L;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        when(reportService.deleteReport(batchNo)).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.deleteReport(batchNo);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).deleteReport(batchNo);
    }

    /**
     * Tests the {@code updateReportStatus} method to ensure successful update of report status.
     * Asserts the response and verifies the service call.
     */
    @Test
    @DisplayName("test_update_report_status_return_success_with_parameter_assertion")
    void testUpdateReportStatus() {
        ReportUpdateDto updateDto = new ReportUpdateDto(ReportStatus.ACTIVE, List.of(1L, 2L, 3L));
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", updateDto);

        when(reportService.updateReportStatus(any(ReportUpdateDto.class))).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.updateReportStatus(updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).updateReportStatus(any(ReportUpdateDto.class));
    }

    /**
     * Tests the {@code fetchReport} method to ensure proper retrieval of reports.
     * Asserts response validity and verifies interactions with the service layer.
     */
    @Test
    @DisplayName("test_fetch_report_return_success_with_parameter_assertion_n_verification")
    void testFetchReport() {
        // Arrange
        final int pageNo = 1;
        final int pageSize = 20;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        // Mocking service layer
        when(reportService.fetchReport(pageNo, pageSize)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GlobalResponse> response = reportController.fetchReport(pageNo, pageSize);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).fetchReport(pageNo, pageSize);
    }
}