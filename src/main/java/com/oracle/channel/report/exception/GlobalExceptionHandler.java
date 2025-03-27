package com.oracle.channel.report.exception;

import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global exception handler to handle application-wide exceptions.
 * Provides custom error responses for different exception types.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles general exceptions that do not fall into specific categories.
     * @param ex the exception thrown
     * @param request the web request object
     * @return a ResponseEntity containing a GlobalResponse with error details
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<GlobalResponse> resolveUnknownHostException(Exception ex, WebRequest request) {

        // Handle DuplicateKeyException separately
        if (ex instanceof DuplicateKeyException duplicateKeyException){
            return this.resolveDuplicateKeyException(duplicateKeyException, request);
        }
        // Handle IllegalArgumentException separately
        if (ex instanceof IllegalArgumentException illegalArgumentException){
            return this.resolveIllegalArgumentException(illegalArgumentException, request);
        }

        // Handle IllegalArgumentException separately
        if (ex instanceof HttpServerErrorException.InternalServerError internalServerError){
            return this.resolveInternalServerError(internalServerError, request);
        }

        // Default error response for other exceptions
        ErrorResponse errorResponse = new ErrorResponse(ResponseCodeEnum.SYS_MALFUNCTION.getCode(),
                "System Malfunction", LocalDateTime.now());

        return new ResponseEntity<>(new GlobalResponse(ResponseCodeEnum.SYS_MALFUNCTION.getCode(), ResponseCodeEnum.SYS_MALFUNCTION.name(),
                errorResponse), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException exceptions.
     * @param ex the exception thrown
     * @param request the web request object
     * @return a ResponseEntity containing a GlobalResponse with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<GlobalResponse> resolveIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        // Creating error response for IllegalArgumentException
        ErrorResponse errorResponse = new ErrorResponse(ResponseCodeEnum.FAILED.getCode(),
                "IllegalArgument parse to request", LocalDateTime.now());

        return new ResponseEntity<>(new GlobalResponse(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.name(),
                errorResponse), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles DuplicateKeyException exceptions.
     * @param ex the exception thrown
     * @param request the web request object
     * @return a ResponseEntity containing a GlobalResponse with error details
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<GlobalResponse> resolveDuplicateKeyException(
            DuplicateKeyException ex, WebRequest request) {

        // Creating error response for DuplicateKeyException
        ErrorResponse errorResponse = new ErrorResponse(ResponseCodeEnum.FAILED.getCode(),
                "DuplicateKeyException", LocalDateTime.now());

        return new ResponseEntity<>(new GlobalResponse(ResponseCodeEnum.DUPLICATE_KEY.getCode(), ResponseCodeEnum.DUPLICATE_KEY.name(),
                errorResponse), HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Handles DuplicateKeyException exceptions.
     * @param ex the exception thrown
     * @param request the web request object
     * @return a ResponseEntity containing a GlobalResponse with error details
     */
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public final ResponseEntity<GlobalResponse> resolveInternalServerError(
            HttpServerErrorException.InternalServerError ex, WebRequest request) {

        // Creating error response for DuplicateKeyException
        ErrorResponse errorResponse = new ErrorResponse(ResponseCodeEnum.FAILED.getCode(),
                "DuplicateKeyException", LocalDateTime.now());

        return new ResponseEntity<>(new GlobalResponse(ResponseCodeEnum.SYS_MALFUNCTION.getCode(), ex.getMessage(),
                errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
