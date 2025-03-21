package com.oracle.channel.report.controllers;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing reports.
 * This class provides endpoints for creating, deleting, updating, and fetching reports.
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportController {

    /**
     * The service layer dependency for handling report operations.
     */
    private final ReportService reportService;

    /**
     * Endpoint to save a new report.
     *
     * @param reportRequestDto the request data for creating a report
     * @return ResponseEntity containing the saved report response
     */
    @PostMapping("/save")
    public ResponseEntity<GlobalResponse> saveReport(@RequestBody @Valid ReportRequestDto reportRequestDto) {
        // Calls the service layer to save the report
        GlobalResponse response = reportService.saveReport(reportRequestDto);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to delete a report by batch number.
     *
     * @param batchNo the batch number of the report to be deleted
     * @return ResponseEntity containing the deletion response
     */
    @DeleteMapping("/delete/{batchNo}")
    public ResponseEntity<GlobalResponse> deleteReport(@PathVariable long batchNo) {
        // Calls the service layer to delete the report by batch number
        GlobalResponse response = reportService.deleteReport(batchNo);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update the status of multiple reports.
     *
     * @param reportUpdateDto the request data containing the batch IDs and new status
     * @return ResponseEntity containing the update response
     */
    @PutMapping("/update-status")
    public ResponseEntity<GlobalResponse> updateReportStatus(@RequestBody @Valid ReportUpdateDto reportUpdateDto) {
        // Calls the service layer to update the report status
        GlobalResponse response = reportService.updateReportStatus(reportUpdateDto);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to fetch a paginated list of reports.
     *
     * @param pageNo the page number (default is 1)
     * @param pageSize the number of records per page (default is 20)
     * @return ResponseEntity containing the paginated report response
     */
    @GetMapping()
    public ResponseEntity<GlobalResponse> fetchReport(@RequestParam(defaultValue = "1", required = false) int pageNo,
                                                      @RequestParam(defaultValue = "20", required = false) int pageSize) {
        // Calls the service layer to fetch reports based on pagination parameters
        GlobalResponse response = reportService.fetchReport(pageNo, pageSize);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }
}
