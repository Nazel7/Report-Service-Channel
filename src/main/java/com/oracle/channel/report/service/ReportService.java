package com.oracle.channel.report.service;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */



public interface ReportService {

    GlobalResponse saveReport(final ReportRequestDto reportRequestDto);

    GlobalResponse deleteReport(long batchNo);

    GlobalResponse updateReportStatus(final ReportUpdateDto reportRequestDto);

    GlobalResponse fetchReport(int pageNo, int pageSize);
}