package com.oracle.channel.report.exception;


import java.time.LocalDateTime;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
public record ErrorResponse(String errorCode,
                            String userMessage,
                            LocalDateTime timeStamp) {


}
