package com.oracle.channel.report.enums;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
public enum ReportStatus {

    ACTIVE("when save report can be fetch"),
    INACTIVE("when report has been shadow deleted and cannot be fetch or display");

    private String desc;

    ReportStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}