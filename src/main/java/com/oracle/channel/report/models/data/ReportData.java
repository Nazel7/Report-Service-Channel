package com.oracle.channel.report.models.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {

    private String name;
    private String desc;
    private Object metadata;
}
