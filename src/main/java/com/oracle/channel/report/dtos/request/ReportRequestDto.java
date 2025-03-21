package com.oracle.channel.report.dtos.request;

import com.oracle.channel.report.models.data.ReportData;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

public record ReportRequestDto(@NotNull Long batchNo,
                               @NotNull Integer size,
                               @NotEmpty List<ReportData> reportData) {

    public ReportRequestDto() {
        this(null, 0, new ArrayList<>());
    }

}
