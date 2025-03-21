package com.oracle.channel.report;

import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.exception.ErrorResponse;
import com.oracle.channel.report.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This test class verifies that the {@code GlobalExceptionHandler} correctly handles various exceptions
 * and returns the expected response codes and messages.
 * </p>
 * <p>
 * Jacoco code coverage should be over 90% with Java 17.
 * </p>
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    /**
     * Initializes the test setup before each test case.
     * <p>
     * - Mocks {@code WebRequest} using Mockito.
     * - Creates an instance of {@code GlobalExceptionHandler}.
     * </p>
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    /**
     * Tests how the exception handler responds to a generic {@link Exception}.
     * <p>
     * Expected response:
     * - Response code: SYS_MALFUNCTION
     * - User message: "System Malfunction"
     * </p>
     */
    @Test
    void testResolveUnknownHostException_GenericException() {
        Exception exception = new Exception("Unknown Error");
        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveUnknownHostException(exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.SYS_MALFUNCTION.getCode(), response.getBody().responseCode());
        assertEquals("System Malfunction", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests how the exception handler responds to a {@link DuplicateKeyException}.
     * <p>
     * Expected response:
     * - Response code: DUPLICATE_KEY
     * - User message: "DuplicateKeyException"
     * </p>
     */
    @Test
    void testResolveUnknownHostException_DuplicateKeyException() {
        DuplicateKeyException exception = new DuplicateKeyException("Duplicate Key Error");
        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveUnknownHostException(exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.DUPLICATE_KEY.getCode(), response.getBody().responseCode());
        assertEquals("DuplicateKeyException", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests how the exception handler responds to an {@link IllegalArgumentException}.
     * <p>
     * Expected response:
     * - Response code: FAILED
     * - User message: "IllegalArgument parse to request"
     * </p>
     */
    @Test
    void testResolveUnknownHostException_IllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal Argument");
        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveUnknownHostException(exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.FAILED.getCode(), response.getBody().responseCode());
        assertEquals("IllegalArgument parse to request", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests how the exception handler responds to an {@link HttpServerErrorException}.
     * <p>
     * Expected response:
     * - HTTP status: 500 (INTERNAL_SERVER_ERROR)
     * - Response code: SYS_MALFUNCTION
     * - User message: "DuplicateKeyException"
     * </p>
     */
    @Test
    void testResolveUnknownHostException_InternalServerError() {
        // Create an InternalServerError exception
        HttpServerErrorException exception = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveUnknownHostException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ResponseCodeEnum.SYS_MALFUNCTION.getCode(), response.getBody().responseCode());
        assertNotNull(response.getBody().error());
        assertTrue(response.getBody().error() instanceof ErrorResponse);
        assertEquals("DuplicateKeyException", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests the handling of {@link IllegalArgumentException} directly.
     * <p>
     * Expected response:
     * - Response code: FAILED
     * - User message: "IllegalArgument parse to request"
     * </p>
     */
    @Test
    void testResolveIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveIllegalArgumentException(exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.FAILED.getCode(), response.getBody().responseCode());
        assertEquals("IllegalArgument parse to request", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests the handling of {@link DuplicateKeyException} directly.
     * <p>
     * Expected response:
     * - Response code: DUPLICATE_KEY
     * - User message: "DuplicateKeyException"
     * </p>
     */
    @Test
    void testResolveDuplicateKeyException() {
        DuplicateKeyException exception = new DuplicateKeyException("Duplicate Key");
        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveDuplicateKeyException(exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.DUPLICATE_KEY.getCode(), response.getBody().responseCode());
        assertEquals("DuplicateKeyException", ((ErrorResponse) response.getBody().error()).userMessage());
    }

    /**
     * Tests how the exception handler responds to an {@link HttpServerErrorException.InternalServerError}.
     * <p>
     * Expected response:
     * - Response code: SYS_MALFUNCTION
     * - User message: "DuplicateKeyException"
     * </p>
     */
    @Test
    void testResolveInternalServerError() {
        // Create an InternalServerError exception
        HttpServerErrorException exception = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        ResponseEntity<GlobalResponse> response = globalExceptionHandler.resolveInternalServerError(
                (HttpServerErrorException.InternalServerError) exception, webRequest);

        assertNotNull(response);
        assertEquals(ResponseCodeEnum.SYS_MALFUNCTION.getCode(), response.getBody().responseCode());
        assertEquals("DuplicateKeyException", ((ErrorResponse) response.getBody().error()).userMessage());
    }
}
