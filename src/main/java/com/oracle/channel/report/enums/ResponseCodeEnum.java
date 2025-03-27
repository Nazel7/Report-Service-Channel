package com.oracle.channel.report.enums;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
public enum ResponseCodeEnum {
    SUCCESS("00"),
    FAILED("01"),
    DUPLICATE_KEY("11"),
    SYS_MALFUNCTION("96");

    private String code;

    ResponseCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}