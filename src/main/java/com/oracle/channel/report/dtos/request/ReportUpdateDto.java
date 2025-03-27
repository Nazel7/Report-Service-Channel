package com.oracle.channel.report.dtos.request;

import com.oracle.channel.report.enums.ReportStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
public record ReportUpdateDto(@NotNull ReportStatus status,
                              @NotEmpty List<Long> batchIds) {
}
