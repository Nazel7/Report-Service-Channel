package com.oracle.channel.report.dtos.response;

import com.oracle.channel.report.dtos.response.data.PageData;
import com.oracle.channel.report.exception.ErrorResponse;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

public record GlobalResponse(String responseCode,
                             String message,
                             Object data,
                             PageData pageData,
                             ErrorResponse error) {


    public GlobalResponse(String responseCode, String message, Object data) {
        this(responseCode, message, data, null, null);
    }

    public GlobalResponse(String responseCode, String message, ErrorResponse error) {
        this(responseCode, message, null, null, error);
    }

    public GlobalResponse(String responseCode, String message, Object data, PageData pageData) {
        this(responseCode, message, data, pageData, null);
    }

}
