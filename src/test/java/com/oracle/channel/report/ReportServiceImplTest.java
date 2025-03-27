/**
 * Unit test class for {@link com.oracle.channel.report.service.impl.ReportServiceImpl}.
 * This class tests various service methods to ensure proper functionality and exception handling.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
package com.oracle.channel.report;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.models.ReportModel;
import com.oracle.channel.report.repository.ReportRepository;
import com.oracle.channel.report.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    private ReportModel reportModel;
    private ReportRequestDto reportRequestDto;
    private ReportUpdateDto reportUpdateDto;

    /**
     * Initializes test data before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportModel = new ReportModel();
        reportModel.setBatchNo(12345L);
        reportModel.setStatus(ReportStatus.ACTIVE);
        reportModel.setId(UUID.randomUUID());

        reportRequestDto = new ReportRequestDto();
        reportUpdateDto = new ReportUpdateDto(ReportStatus.INACTIVE, List.of(12345L));
    }

    /**
     * Tests successful report saving.
     */
    @Test
    void testSaveReport_Success() {
        when(reportRepository.save(any(ReportModel.class))).thenReturn(reportModel);
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportModel, response.data());
        verify(reportRepository, times(1)).save(any(ReportModel.class));
    }

    /**
     * Tests handling of duplicate key exception during report saving.
     */
    @Test
    void testSaveReport_DuplicateKeyException() {
        when(reportRepository.save(any(ReportModel.class))).thenThrow(new DuplicateKeyException("Duplicate entry"));
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.DUPLICATE_KEY.getCode(), response.responseCode());
        assertNotNull(response.error());
    }

    /**
     * Tests successful report deletion.
     */
    @Test
    void testDeleteReport_Success() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(reportModel);
        doNothing().when(reportRepository).delete(reportModel);

        GlobalResponse response = reportService.deleteReport(12345L);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(12345L, response.data());
        verify(reportRepository, times(1)).delete(reportModel);
    }

    /**
     * Tests report deletion when the report is not found.
     */
    @Test
    void testDeleteReport_NotFound() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> reportService.deleteReport(12345L));
    }

    /**
     * Tests successful report status update.
     */
    @Test
    void testUpdateReportStatus_Success() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(List.of(reportModel));
        when(reportRepository.saveAll(anyList())).thenReturn(List.of(reportModel));

        GlobalResponse response = reportService.updateReportStatus(reportUpdateDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(1, ((List<?>) response.data()).size());
    }

    /**
     * Tests report status update when the report is not found.
     */
    @Test
    void testUpdateReportStatus_NotFound() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> reportService.updateReportStatus(reportUpdateDto));
    }

    /**
     * Tests successful fetching of reports.
     */
    @Test
    void testFetchReport_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReportModel> reportPage = new PageImpl<>(List.of(reportModel));
        when(reportRepository.getAllByReport(pageable)).thenReturn(reportPage);

        GlobalResponse response = reportService.fetchReport(1, 10);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportPage, response.data());
    }

    /**
     * Tests handling of exceptions during report fetching.
     */
    @Test
    void testFetchReport_Exception() {
        Pageable pageable = PageRequest.of(0, 10);
        when(reportRepository.getAllByReport(pageable)).thenThrow(new RuntimeException("Fetch failed"));
        assertThrows(IllegalArgumentException.class, () -> reportService.fetchReport(1, 10));
    }
}
