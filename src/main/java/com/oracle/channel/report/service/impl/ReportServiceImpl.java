package com.oracle.channel.report.service.impl;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.exception.ErrorResponse;
import com.oracle.channel.report.models.ReportModel;
import com.oracle.channel.report.repository.ReportRepository;
import com.oracle.channel.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the ReportService interface, providing methods for managing reports.
 * This class handles CRUD operations related to reports.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final String REQ_LOG = "[data: {},\nRequestTime: {}]";
    private static final String RES_LOG = "[data: {},\nResponseTime: {}]";
    private final ReportRepository reportRepository;

    /**
     * Saves a new report in the database.
     *
     * @param reportRequestDto the request data transfer object containing report details
     * @return GlobalResponse indicating success or failure of the operation
     */
    @Override
    public GlobalResponse saveReport(final ReportRequestDto reportRequestDto) {
        log.info("SaveReportRequest: {}", reportRequestDto);
        try {
            // Convert DTO to ReportModel and save in repository
            ReportModel reportModel = reportRepository.save(ReportModel.parse(reportRequestDto));
            log.info("SavedReport: ".concat(REQ_LOG), reportModel, LocalDateTime.now());

            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), reportModel);
        } catch (DuplicateKeyException ex) {
            log.info("Error saving Report: {}", ex.getMessage());
           return new GlobalResponse(ResponseCodeEnum.DUPLICATE_KEY.getCode(), ResponseCodeEnum.DUPLICATE_KEY.name(),
                   new ErrorResponse(ResponseCodeEnum.FAILED.getCode(),
                   "Duplicate Key Exception", LocalDateTime.now()));
        }
    }

    /**
     * Deletes a report based on the provided batch number.
     *
     * @param batchNo the batch number of the report to be deleted
     * @return GlobalResponse indicating success or failure of the deletion
     */
    @Override
    public GlobalResponse deleteReport(final long batchNo) {
        log.info("ReportDeleteRequest: ".concat(REQ_LOG), batchNo, LocalDateTime.now());
        try {
            ReportModel reportModel = reportRepository.getReportModelByBatchNo(batchNo);
            if (reportModel == null) {
                log.warn("Illegal batchNo");
                throw new IllegalArgumentException("Error Report does not exist: ");
            }
            reportRepository.delete(reportModel);
            log.info("ReportDeleted: ".concat(RES_LOG), reportModel.getBatchNo(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), batchNo);
        } catch (Exception ex) {
            log.info("Error DeletingReport: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * Updates the status of reports based on batch IDs.
     *
     * @param reportRequestDto the DTO containing the new status and batch IDs to update
     * @return GlobalResponse indicating success or failure of the update operation
     */
    @Override
    public GlobalResponse updateReportStatus(final ReportUpdateDto reportRequestDto) {
        log.info("ReportUpdateRequest: ".concat(REQ_LOG), reportRequestDto, LocalDateTime.now());
        try {
            List<ReportModel> reportModels = reportRepository.fetchReportModelList(reportRequestDto.batchIds());
            if (reportModels.isEmpty()) {
                log.warn("Illegal argument");
                throw new IllegalArgumentException("Illegal argument");
            }

            List<ReportModel> modelupdatedList =
                    reportModels.stream().peek(model -> model.setStatus(reportRequestDto.status())).toList();
            modelupdatedList = reportRepository.saveAll(modelupdatedList);
            log.info("ReportUpdated: ".concat(RES_LOG), reportRequestDto.batchIds(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), modelupdatedList);
        } catch (Exception ex) {
            log.info("Error Update Report: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * Fetches a paginated list of reports.
     *
     * @param pageNo the page number to retrieve
     * @param pageSize the number of reports per page
     * @return GlobalResponse containing a paginated list of reports
     */
    @Override
    public GlobalResponse fetchReport(final int pageNo, final int pageSize) {
        log.info("FetchReportRequest: [pageNo= {},\npageSize= {},\nReqTime: {}]", pageNo, pageSize, LocalDateTime.now());
        Pageable pageable = PageRequest.of(pageNo < 1 ? pageNo : pageNo - 1, pageSize);
        try {
            Page<ReportModel> reportModelPage = reportRepository.getAllByReport(pageable);
            log.info("FetchedDataSize: ".concat(RES_LOG), reportModelPage.getSize(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), reportModelPage);
        } catch (Exception ex) {
            log.info("Error Fetching Report: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}
