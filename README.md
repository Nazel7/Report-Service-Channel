
`0. UseCase`
Description:
A financial system generates reports daily. The ReportRepository interface stores and retrieves metadata (batch number, status, timestamp) for efficient management.

Actors:
- System – Stores and fetches report metadata.
- Admin Users – Manage and view reports.
- Automated System – Generates reports.

Flow:
A report is generated.
Metadata is saved via ReportRepository.
Admins or systems retrieve metadata when needed.
Errors are handled (e.g., duplicates, missing records).
Outcome:
Report metadata is securely stored and accessible for audits and tracking.

`1. Prompt`
### Prompt Title
Spring Boot Report Repository for Storing and Managing Report Metadata

### High-Level Description
Develop a Spring Boot @Repository interface called ReportRepository to manage the storage and retrieval of generated report metadata. This repository will interact with a database to:

1. Store metadata for reports, including batch numbers, statuses,      and timestamps.
2. Retrieve reports based on batch numbers and status filters.
   Support paginated queries for efficient data retrieval.
3. Enable updating report statuses for better data management.


### Functions / Classes to Be Created by LLM

## 1. ReportRepository.java

# Functionality:

`getReportModelByBatchNo(long batchNo):` Retrieves a report by its batch number.
`getAllByReport(Pageable pageable):` Fetches paginated active reports.

`fetchReportModelList(List<Long> batchIds):` Retrieves reports matching a list of batch numbers.

# Annotations:<br>
Uses `@Repository` for Spring Data JPA integration.
Uses `@Query` for custom database queries.

## 2.ReportService.java

# Functionality:

`saveReport(ReportRequestDto request):` Saves new report metadata.

`deleteReport(long batchNo):` Deletes a report by batch number.

`updateReportStatus(ReportUpdateDto request):` Updates report statuses in bulk.

`fetchReport(int pageNo, int pageSize):` Retrieves paginated reports.

# Annotations:
Uses `@Service` for business logic layer. also
handles exceptions for database operations.

## 3.ReportController.java

# REST API Endpoints:

`POST /api/v1.0/reports/save:` Save report metadata.
`DELETE /api/v1.0/reports/delete/{batchNo}:` Delete a report by batch number.
`PUT /api/v1.0/reports/update-status:` Update report statuses.
`GET /api/v1.0/reports:` Fetch paginated reports.

# Validates:
Hibernate validator used for Input validation (`@Valid`).

# Proper Endpoint Structure.

# i.Save
`@PostMapping("/save")`
public ResponseEntity<GlobalResponse> saveReport(`@RequestBody @Valid` ReportRequestDto reportRequestDto) {...}

# ii. delete
`@DeleteMapping("/delete/{batchNo}")`
public ResponseEntity<GlobalResponse> deleteReport(`@PathVariable` long batchNo){...}

# iii. update-status
`@PutMapping("/update-status")`
public ResponseEntity<GlobalResponse> updateReportStatus(`@RequestBody @Valid` ReportUpdateDto reportUpdateDto) {...}

# iv. fetchReport paginated
`@GetMapping()`
public ResponseEntity<GlobalResponse> fetchReport(`@RequestParam(defaultValue = "1", required = false)` int pageNo, `@RequestParam(defaultValue = "20", required = false)` int pageSize) {...}

## 4.GlobalExceptionHandler.java
Handles exceptions globally using `@RestControllerAdvice`

`IllegalArgumentException` → 404 NOT FOUND.<br>
`DuplicateKeyExcpetion` → 500 INTERNAL SERVER ERROR.
`Generic exceptions` → 500 INTERNAL SERVER ERROR.

## 5.Tests

- Uses an postgreSQL for testing.
- Verifies correct CRUD operations on reports.
- Ensures proper query execution.
- Unit Tests (ReportServiceTest.java, ReportControllerTest.java)
- Mocks repository interactions with Mockito.
- Tests service methods for saving, updating, and deleting reports.
- Uses MockMvc to validate API request handling.

## 6. Dependencies Use

- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-boot-starter-validation
- postgresql-42.7.5
- lombok (for reducing boilerplate code)
- slf4j (for logging)
- junit 5 and mockito (for unit testing)
- jacoco-maven-plugin (for test coverage)
- spring-boot-starter-actuator
- Testing the Whole Function

## 7. Unit Tests

- Service Layer: Mocks repository methods and validates business logic.

- Controller Layer: Uses MockMvc to test REST API responses.
- Exception Handling: Ensures proper error messages are returned,
  and Tested end-to-end from repository, service and Controller layer with their interactions to one another.


`2. Code`
# Project Structure

```plaintext
report-repository/
│── src/
│   ├── main/
│   │   ├── java/com/oracle/channel/report/
│   │   │   ├── controller/              
│   │   │   │   ├── ReportController.java       # Exposes REST API endpoints
│   │   │   ├── dto/                    
│   │   │   │   ├── request/          
│   │   │   │   │   ├── ReportRequestDto.java   # DTO for report creation
│   │   │   │   │   ├── ReportUpdateDto.java    # DTO for updating report status
│   │   │   │   ├── response/        
│   │   │   │   │   ├── GlobalResponse.java     # Standardized API response wrapper
│   │   │   ├── enums/                  
│   │   │   │   ├── ReportStatus.java           # Enum for report statuses
│   │   │   │   ├── ResponseCodeEnum.java       # Enum for response codes
│   │   │   ├── exception/              
│   │   │   │   ├── GlobalExceptionHandler.java # Handles application exceptions globally
│   │   │   │   ├── ReportNotFoundException.java # Custom exception for missing reports
│   │   │   ├── model/                  
│   │   │   │   ├── ReportModel.java            # Entity class representing reports
│   │   │   ├── repository/             
│   │   │   │   ├── ReportRepository.java       # Spring Data JPA repository interface
│   │   │   ├── service/                
│   │   │   │   ├── ReportService.java          # Service interface for report operations
│   │   │   │   ├── impl/              
│   │   │   │   │   ├── ReportServiceImpl.java  # Implementation of report business logic
│   │   │   ├── util/                    
│   │   │   │   ├── DateUtil.java               # Utility class for handling dates
│   │   │   │   ├── ValidationUtil.java         # Utility class for validating input
│   │   ├── resources/
│   │   │   ├── application.yml                 # Database, logging, and other configurations
│   │   │   ├── logback-spring.xml              # Custom logging configuration
│   │   │   ├── data.sql                        # Sample SQL data for testing
│   │   │   ├── schema.sql                      # Database schema (optional)
│   ├── test/java/com/oracle/channel/report/
│   │   ├── controller/
│   │   │   ├── ReportControllerTest.java       # Tests API endpoints using MockMvc
│   │   ├── exceptionhandler/
│   │   │   ├── GlobalExceptionHandlerTest.java       # Tests GlobalExceptionHandler layer using @RestControllerAdvice
│   │   ├── service/
│   │   │   ├── ReportServiceTest.java          # Tests service  with embedded PostgreSQL
│── pom.xml                                      # Maven dependencies
│── README.md                                    # Project setup and API documentation
│── .gitignore                                   # Ignore files like logs, .idea/, target/


```

<br>

1. **ReportServiceApplication.java**: `src/main/java/com/oracle/channel/report/ReportServiceApplication.java`

```java
package com.oracle.channel.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.oracle.channel")
public class ReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServiceApplication.class, args);
	}

}

```

2. **ReportController.java**: `src/main/java/com/oracle/channel/report/controllers/ReportController.java`

```java
package com.oracle.channel.report.controllers;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing reports.
 * This class provides endpoints for creating, deleting, updating, and fetching reports.
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportController {

    /**
     * The service layer dependency for handling report operations.
     */
    private final ReportService reportService;

    /**
     * Endpoint to save a new report.
     *
     * @param reportRequestDto the request data for creating a report
     * @return ResponseEntity containing the saved report response
     */
    @PostMapping("/save")
    public ResponseEntity<GlobalResponse> saveReport(@RequestBody @Valid ReportRequestDto reportRequestDto) {
        // Calls the service layer to save the report
        GlobalResponse response = reportService.saveReport(reportRequestDto);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to delete a report by batch number.
     *
     * @param batchNo the batch number of the report to be deleted
     * @return ResponseEntity containing the deletion response
     */
    @DeleteMapping("/delete/{batchNo}")
    public ResponseEntity<GlobalResponse> deleteReport(@PathVariable long batchNo) {
        // Calls the service layer to delete the report by batch number
        GlobalResponse response = reportService.deleteReport(batchNo);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update the status of multiple reports.
     *
     * @param reportUpdateDto the request data containing the batch IDs and new status
     * @return ResponseEntity containing the update response
     */
    @PutMapping("/update-status")
    public ResponseEntity<GlobalResponse> updateReportStatus(@RequestBody @Valid ReportUpdateDto reportUpdateDto) {
        // Calls the service layer to update the report status
        GlobalResponse response = reportService.updateReportStatus(reportUpdateDto);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to fetch a paginated list of reports.
     *
     * @param pageNo the page number (default is 1)
     * @param pageSize the number of records per page (default is 20)
     * @return ResponseEntity containing the paginated report response
     */
    @GetMapping()
    public ResponseEntity<GlobalResponse> fetchReport(@RequestParam(defaultValue = "1", required = false) int pageNo,
                                                      @RequestParam(defaultValue = "20", required = false) int pageSize) {
        // Calls the service layer to fetch reports based on pagination parameters
        GlobalResponse response = reportService.fetchReport(pageNo, pageSize);
        // Returns the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }
}


```

3. **ReportServiceImpl.java**: `src/main/java/com/oracle/channel/report/service/impl/ReportServiceImpl.java`

```java
package com.oracle.channel.report.service.impl;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.exception.ErrorResponse;
import com.oracle.channel.report.models.ReportModel;
import com.oracle.channel.report.repository.ReportRepository;
import com.oracle.channel.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the ReportService interface, providing methods for managing reports.
 * This class handles CRUD operations related to reports.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final String REQ_LOG = "[data: {},\nRequestTime: {}]";
    private static final String RES_LOG = "[data: {},\nResponseTime: {}]";
    private final ReportRepository reportRepository;

    /**
     * Saves a new report in the database.
     *
     * @param reportRequestDto the request data transfer object containing report details
     * @return GlobalResponse indicating success or failure of the operation
     */
    @Override
    public GlobalResponse saveReport(final ReportRequestDto reportRequestDto) {
        log.info("SaveReportRequest: {}", reportRequestDto);
        try {
            // Convert DTO to ReportModel and save in repository
            ReportModel reportModel = reportRepository.save(ReportModel.parse(reportRequestDto));
            log.info("SavedReport: ".concat(REQ_LOG), reportModel, LocalDateTime.now());

            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), reportModel);
        } catch (DuplicateKeyException ex) {
            log.info("Error saving Report: {}", ex.getMessage());
           return new GlobalResponse(ResponseCodeEnum.DUPLICATE_KEY.getCode(), ResponseCodeEnum.DUPLICATE_KEY.name(),
                   new ErrorResponse(ResponseCodeEnum.FAILED.getCode(),
                   "Duplicate Key Exception", LocalDateTime.now()));
        }
    }

    /**
     * Deletes a report based on the provided batch number.
     *
     * @param batchNo the batch number of the report to be deleted
     * @return GlobalResponse indicating success or failure of the deletion
     */
    @Override
    public GlobalResponse deleteReport(final long batchNo) {
        log.info("ReportDeleteRequest: ".concat(REQ_LOG), batchNo, LocalDateTime.now());
        try {
            ReportModel reportModel = reportRepository.getReportModelByBatchNo(batchNo);
            if (reportModel == null) {
                log.warn("Illegal batchNo");
                throw new IllegalArgumentException("Error Report does not exist: ");
            }
            reportRepository.delete(reportModel);
            log.info("ReportDeleted: ".concat(RES_LOG), reportModel.getBatchNo(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), batchNo);
        } catch (Exception ex) {
            log.info("Error DeletingReport: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * Updates the status of reports based on batch IDs.
     *
     * @param reportRequestDto the DTO containing the new status and batch IDs to update
     * @return GlobalResponse indicating success or failure of the update operation
     */
    @Override
    public GlobalResponse updateReportStatus(final ReportUpdateDto reportRequestDto) {
        log.info("ReportUpdateRequest: ".concat(REQ_LOG), reportRequestDto, LocalDateTime.now());
        try {
            List<ReportModel> reportModels = reportRepository.fetchReportModelList(reportRequestDto.batchIds());
            if (reportModels.isEmpty()) {
                log.warn("Illegal argument");
                throw new IllegalArgumentException("Illegal argument");
            }

            List<ReportModel> modelupdatedList =
                    reportModels.stream().peek(model -> model.setStatus(reportRequestDto.status())).toList();
            modelupdatedList = reportRepository.saveAll(modelupdatedList);
            log.info("ReportUpdated: ".concat(RES_LOG), reportRequestDto.batchIds(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), modelupdatedList);
        } catch (Exception ex) {
            log.info("Error Update Report: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * Fetches a paginated list of reports.
     *
     * @param pageNo the page number to retrieve
     * @param pageSize the number of reports per page
     * @return GlobalResponse containing a paginated list of reports
     */
    @Override
    public GlobalResponse fetchReport(final int pageNo, final int pageSize) {
        log.info("FetchReportRequest: [pageNo= {},\npageSize= {},\nReqTime: {}]", pageNo, pageSize, LocalDateTime.now());
        Pageable pageable = PageRequest.of(pageNo < 1 ? pageNo : pageNo - 1, pageSize);
        try {
            Page<ReportModel> reportModelPage = reportRepository.getAllByReport(pageable);
            log.info("FetchedDataSize: ".concat(RES_LOG), reportModelPage.getSize(), LocalDateTime.now());
            return new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.name(), reportModelPage);
        } catch (Exception ex) {
            log.info("Error Fetching Report: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}


```

4. **ReportService.java**: `src/main/java/com/oracle/channel/report/service/ReportService.java`

```java
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

```


5. **ReportRepository.java**: `src/main/java/com/oracle/channel/report/repository/ReportRepository.java`

```java
package com.oracle.channel.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.oracle.channel")
public class ReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServiceApplication.class, args);
	}

}

```


6. **ReportRequestDto.java**: `src/main/java/com/oracle/channel/report/dtos/request/ReportRequestDto.java`

```java
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

```

7. **ReportUpdateDto.java**: `src/main/java/com/oracle/channel/report/dtos/request/ReportUpdateDto.java`

```java
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

```

8. **PageData.java**: `src/main/java/com/oracle/channel/report/dtos/response/data/PageData.java`

```java
package com.oracle.channel.report.dtos.response.data;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

public record PageData(int pageNo, int size) {

}

```

9. **GlobalResponse.java**: `src/main/java/com/oracle/channel/report/dtos/response/GlobalResponse.java`

```java
package com.oracle.channel.report.dtos.response.data;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

public record PageData(int pageNo, int size) {

}

```

10. **ReportStatus.java**: `src/main/java/com/oracle/channel/report/enums/ReportStatus.java`

```java
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

```

12. **ErrorResponse.java**: `src/main/java/com/oracle/channel/report/exception/ErrorResponse.java`

```java
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

```

13. **GlobalExceptionHandler.java**: `src/main/java/com/oracle/channel/report/exception/GlobalExceptionHandler.java`

```java
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

```

14. **ReportData.java**: `src/main/java/com/oracle/channel/report/models/data/ReportData.java`

```java
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

```

15. **ReportModel.java**: `src/main/java/com/oracle/channel/report/models/ReportModel.java`

```java
package com.oracle.channel.report.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.models.data.ReportData;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author GafarOlanipekun
 * @since 19/03/2025
 */

@Entity
@Table(name = "report", indexes = {
        @Index(name = "batch_id_index", columnList = "batchNo, id" )
})
@Access(AccessType.FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@Slf4j
public class ReportModel {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "batch_no", unique = true, updatable = false, nullable = false, length = 24)
    private Long batchNo;

    private Integer size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<ReportData> reportData = new ArrayList<>();

    @CreationTimestamp
//    @Setter(AccessLevel.NONE)
    @Column(length = 48)
    private Date createdAt;

    @UpdateTimestamp
//    @Setter(AccessLevel.NONE)
    @Column(length = 48)
    private Date updatedAt;


    public static ReportModel parse(ReportRequestDto reportRequestDto) {

        ReportModel model;
        try {
            ObjectMapper mapper = new ObjectMapper();
            model = mapper.convertValue(reportRequestDto, ReportModel.class);
            model.setStatus(ReportStatus.ACTIVE);
        }catch (IllegalArgumentException ex) {
           log.info("ReportParseError: {}", ex.getMessage());
          throw new IllegalArgumentException(ex.getMessage());
        }

        return model;
    }

}

```
16. **application-dev.yml**: <br>`src/main/resources/application-dev.yml`

```java
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/report_db?prepareThreshold=0
    username: postgres
    password: passcode
    driver-class-name: org.postgresql.Driver
    hikari:
      minimumIdle: 2
      maximumPoolSize: 10
      idleTimeout: 30000
      connectionTimeout: 600000
      leakDetectionThreshold: 300000
  jpa:
    hibernate:
      ddl-auto: update
      transaction:
        jta:
          platform:generate-ddl: true
    defer-datasource-initialization: true
    properties:
      hibernate:
        dialet: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
management:
  endpoints:
    web:
      exposure:
        include: info, health, metrics
  endpoint:
    health:
      show-details: always
  metrics:
    distribution:
      slo:
        http:
          server:
            requests: 100ms, 150ms, 250ms, 500ms, 1s
  health:
    readinessstate:
      enabled: true
    livenessstate:
      enabled: true


```

17. **application.yml**: <br>`src/main/resources/application.yml`

```java
server:
  port: 8080
  servlet:
    context-path: /api/v1.0

spring:
  application:
    name: '@project.name@'
  profiles:
    active: dev


```

`3. unit test`


# 1st Test Iteration

1. **ReportControllerTest.java**: `src/test/java/com/oracle/channel/report/ReportDataControllerTest.java`
```java
package com.oracle.channel.report;

import com.oracle.channel.report.controllers.ReportController;
import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ReportController} class.
 * This class tests the controller methods responsible for handling report operations.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
class ReportDataControllerTest {

    /**
     * The controller instance under test.
     */
    @InjectMocks
    private ReportController reportController;

    /**
     * Mocked instance of {@link ReportService} to simulate service layer operations.
     */
    @Mock
    private ReportService reportService;

    /**
     * Sets up test environment before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code saveReport} method to ensure successful report saving.
     * Validates response status and returned object.
     */
    @Test
    @DisplayName("test_save_report_return_success_with_parameter_assertion_n_verification")
    void testSaveReport() {
        // Create a mock request DTO
        ReportRequestDto requestDto = new ReportRequestDto();
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", requestDto);

        // Mock the service layer response
        when(reportService.saveReport(any(ReportRequestDto.class))).thenReturn(expectedResponse);

        // Call the controller method
        ResponseEntity<GlobalResponse> response = reportController.saveReport(requestDto);

        // Assert that the response status is 200 (OK)
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());

        // Verify that the service method was called exactly once
        verify(reportService, times(1)).saveReport(any(ReportRequestDto.class));
    }

    /**
     * Tests the {@code deleteReport} method to ensure successful report deletion.
     * Checks that the service method is called and the response is valid.
     */
    @Test
    void testDeleteReport() {
        final long batchNo = 12345L;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        when(reportService.deleteReport(batchNo)).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.deleteReport(batchNo);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).deleteReport(batchNo);
    }

    /**
     * Tests the {@code updateReportStatus} method to ensure successful update of report status.
     * Asserts the response and verifies the service call.
     */
    @Test
    @DisplayName("test_update_report_status_return_success_with_parameter_assertion")
    void testUpdateReportStatus() {
        ReportUpdateDto updateDto = new ReportUpdateDto(ReportStatus.ACTIVE, List.of(1L, 2L, 3L));
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", updateDto);

        when(reportService.updateReportStatus(any(ReportUpdateDto.class))).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.updateReportStatus(updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).updateReportStatus(any(ReportUpdateDto.class));
    }

    /**
     * Tests the {@code fetchReport} method to ensure proper retrieval of reports.
     * Asserts response validity and verifies interactions with the service layer.
     */
    @Test
    @DisplayName("test_fetch_report_return_success_with_parameter_assertion_n_verification")
    void testFetchReport() {
        // Arrange
        final int pageNo = 1;
        final int pageSize = 20;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        // Mocking service layer
        when(reportService.fetchReport(pageNo, pageSize)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GlobalResponse> response = reportController.fetchReport(pageNo, pageSize);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).fetchReport(pageNo, pageSize);
    }
}
```

2. **ReportServiceImplTest.java**: `src/test/java/com/oracle/channel/report/ReportServiceImplTest.java`

```java
/**
 * Unit test class for {@link com.oracle.channel.report.service.impl.ReportServiceImpl}.
 * This class tests various service methods to ensure proper functionality and exception handling.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
package com.oracle.channel.report;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.models.ReportModel;
import com.oracle.channel.report.repository.ReportRepository;
import com.oracle.channel.report.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    private ReportModel reportModel;
    private ReportRequestDto reportRequestDto;
    private ReportUpdateDto reportUpdateDto;

    /**
     * Initializes test data before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportModel = new ReportModel();
        reportModel.setBatchNo(12345L);
        reportModel.setStatus(ReportStatus.ACTIVE);
        reportModel.setId(UUID.randomUUID());

        reportRequestDto = new ReportRequestDto();
        reportUpdateDto = new ReportUpdateDto(ReportStatus.INACTIVE, List.of(12345L));
    }

    /**
     * Tests successful report saving.
     */
    @Test
    void testSaveReport_Success() {
        when(reportRepository.save(any(ReportModel.class))).thenReturn(reportModel);
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportModel, response.data());
        verify(reportRepository, times(1)).save(any(ReportModel.class));
    }

    /**
     * Tests handling of duplicate key exception during report saving.
     */
    @Test
    void testSaveReport_DuplicateKeyException() {
        when(reportRepository.save(any(ReportModel.class))).thenThrow(new DuplicateKeyException("Duplicate entry"));
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.DUPLICATE_KEY.getCode(), response.responseCode());
        assertNotNull(response.error());
    }

    /**
     * Tests successful report deletion.
     */
    @Test
    void testDeleteReport_Success() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(reportModel);
        doNothing().when(reportRepository).delete(reportModel);

        GlobalResponse response = reportService.deleteReport(12345L);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(12345L, response.data());
        verify(reportRepository, times(1)).delete(reportModel);
    }

    /**
     * Tests report deletion when the report is not found.
     */
    @Test
    void testDeleteReport_NotFound() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> reportService.deleteReport(12345L));
    }

    /**
     * Tests successful report status update.
     */
    @Test
    void testUpdateReportStatus_Success() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(List.of(reportModel));
        when(reportRepository.saveAll(anyList())).thenReturn(List.of(reportModel));

        GlobalResponse response = reportService.updateReportStatus(reportUpdateDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(1, ((List<?>) response.data()).size());
    }

    /**
     * Tests report status update when the report is not found.
     */
    @Test
    void testUpdateReportStatus_NotFound() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> reportService.updateReportStatus(reportUpdateDto));
    }

    /**
     * Tests successful fetching of reports.
     */
    @Test
    void testFetchReport_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReportModel> reportPage = new PageImpl<>(List.of(reportModel));
        when(reportRepository.getAllByReport(pageable)).thenReturn(reportPage);

        GlobalResponse response = reportService.fetchReport(1, 10);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportPage, response.data());
    }

    /**
     * Tests handling of exceptions during report fetching.
     */
    @Test
    void testFetchReport_Exception() {
        Pageable pageable = PageRequest.of(0, 10);
        when(reportRepository.getAllByReport(pageable)).thenThrow(new RuntimeException("Fetch failed"));
        assertThrows(IllegalArgumentException.class, () -> reportService.fetchReport(1, 10));
    }
}


```
# 2nd Test Iteration
1. **GlobalExceptionHandlerTest.java**: `src/test/java/com/oracle/channel/report/GlobalExceptionHandlerTest.java`

```java
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


```
2. **ReportControllerTest.java**: `src/test/java/com/oracle/channel/report/ReportDataControllerTest.java`
```java
package com.oracle.channel.report;

import com.oracle.channel.report.controllers.ReportController;
import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ReportController} class.
 * This class tests the controller methods responsible for handling report operations.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
class ReportDataControllerTest {

    /**
     * The controller instance under test.
     */
    @InjectMocks
    private ReportController reportController;

    /**
     * Mocked instance of {@link ReportService} to simulate service layer operations.
     */
    @Mock
    private ReportService reportService;

    /**
     * Sets up test environment before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code saveReport} method to ensure successful report saving.
     * Validates response status and returned object.
     */
    @Test
    @DisplayName("test_save_report_return_success_with_parameter_assertion_n_verification")
    void testSaveReport() {
        // Create a mock request DTO
        ReportRequestDto requestDto = new ReportRequestDto();
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", requestDto);

        // Mock the service layer response
        when(reportService.saveReport(any(ReportRequestDto.class))).thenReturn(expectedResponse);

        // Call the controller method
        ResponseEntity<GlobalResponse> response = reportController.saveReport(requestDto);

        // Assert that the response status is 200 (OK)
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());

        // Verify that the service method was called exactly once
        verify(reportService, times(1)).saveReport(any(ReportRequestDto.class));
    }

    /**
     * Tests the {@code deleteReport} method to ensure successful report deletion.
     * Checks that the service method is called and the response is valid.
     */
    @Test
    void testDeleteReport() {
        final long batchNo = 12345L;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        when(reportService.deleteReport(batchNo)).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.deleteReport(batchNo);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).deleteReport(batchNo);
    }

    /**
     * Tests the {@code updateReportStatus} method to ensure successful update of report status.
     * Asserts the response and verifies the service call.
     */
    @Test
    @DisplayName("test_update_report_status_return_success_with_parameter_assertion")
    void testUpdateReportStatus() {
        ReportUpdateDto updateDto = new ReportUpdateDto(ReportStatus.ACTIVE, List.of(1L, 2L, 3L));
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", updateDto);

        when(reportService.updateReportStatus(any(ReportUpdateDto.class))).thenReturn(expectedResponse);

        ResponseEntity<GlobalResponse> response = reportController.updateReportStatus(updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).updateReportStatus(any(ReportUpdateDto.class));
    }

    /**
     * Tests the {@code fetchReport} method to ensure proper retrieval of reports.
     * Asserts response validity and verifies interactions with the service layer.
     */
    @Test
    @DisplayName("test_fetch_report_return_success_with_parameter_assertion_n_verification")
    void testFetchReport() {
        // Arrange
        final int pageNo = 1;
        final int pageSize = 20;
        GlobalResponse expectedResponse = new GlobalResponse(ResponseCodeEnum.SUCCESS.getCode(), "Success", null);

        // Mocking service layer
        when(reportService.fetchReport(pageNo, pageSize)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GlobalResponse> response = reportController.fetchReport(pageNo, pageSize);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
        verify(reportService, times(1)).fetchReport(pageNo, pageSize);
    }
}
```

3. **ReportServiceImplTest.java**: `src/test/java/com/oracle/channel/report/ReportServiceImplTest.java`

```java
/**
 * Unit test class for {@link com.oracle.channel.report.service.impl.ReportServiceImpl}.
 * This class tests various service methods to ensure proper functionality and exception handling.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
package com.oracle.channel.report;

import com.oracle.channel.report.dtos.request.ReportRequestDto;
import com.oracle.channel.report.dtos.request.ReportUpdateDto;
import com.oracle.channel.report.dtos.response.GlobalResponse;
import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.enums.ResponseCodeEnum;
import com.oracle.channel.report.models.ReportModel;
import com.oracle.channel.report.repository.ReportRepository;
import com.oracle.channel.report.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    private ReportModel reportModel;
    private ReportRequestDto reportRequestDto;
    private ReportUpdateDto reportUpdateDto;

    /**
     * Initializes test data before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportModel = new ReportModel();
        reportModel.setBatchNo(12345L);
        reportModel.setStatus(ReportStatus.ACTIVE);
        reportModel.setId(UUID.randomUUID());

        reportRequestDto = new ReportRequestDto();
        reportUpdateDto = new ReportUpdateDto(ReportStatus.INACTIVE, List.of(12345L));
    }

    /**
     * Tests successful report saving.
     */
    @Test
    void testSaveReport_Success() {
        when(reportRepository.save(any(ReportModel.class))).thenReturn(reportModel);
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportModel, response.data());
        verify(reportRepository, times(1)).save(any(ReportModel.class));
    }

    /**
     * Tests handling of duplicate key exception during report saving.
     */
    @Test
    void testSaveReport_DuplicateKeyException() {
        when(reportRepository.save(any(ReportModel.class))).thenThrow(new DuplicateKeyException("Duplicate entry"));
        GlobalResponse response = reportService.saveReport(reportRequestDto);

        assertEquals(ResponseCodeEnum.DUPLICATE_KEY.getCode(), response.responseCode());
        assertNotNull(response.error());
    }

    /**
     * Tests successful report deletion.
     */
    @Test
    void testDeleteReport_Success() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(reportModel);
        doNothing().when(reportRepository).delete(reportModel);

        GlobalResponse response = reportService.deleteReport(12345L);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(12345L, response.data());
        verify(reportRepository, times(1)).delete(reportModel);
    }

    /**
     * Tests report deletion when the report is not found.
     */
    @Test
    void testDeleteReport_NotFound() {
        when(reportRepository.getReportModelByBatchNo(12345L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> reportService.deleteReport(12345L));
    }

    /**
     * Tests successful report status update.
     */
    @Test
    void testUpdateReportStatus_Success() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(List.of(reportModel));
        when(reportRepository.saveAll(anyList())).thenReturn(List.of(reportModel));

        GlobalResponse response = reportService.updateReportStatus(reportUpdateDto);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(1, ((List<?>) response.data()).size());
    }

    /**
     * Tests report status update when the report is not found.
     */
    @Test
    void testUpdateReportStatus_NotFound() {
        when(reportRepository.fetchReportModelList(anyList())).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> reportService.updateReportStatus(reportUpdateDto));
    }

    /**
     * Tests successful fetching of reports.
     */
    @Test
    void testFetchReport_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReportModel> reportPage = new PageImpl<>(List.of(reportModel));
        when(reportRepository.getAllByReport(pageable)).thenReturn(reportPage);

        GlobalResponse response = reportService.fetchReport(1, 10);

        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.responseCode());
        assertEquals(reportPage, response.data());
    }

    /**
     * Tests handling of exceptions during report fetching.
     */
    @Test
    void testFetchReport_Exception() {
        Pageable pageable = PageRequest.of(0, 10);
        when(reportRepository.getAllByReport(pageable)).thenThrow(new RuntimeException("Fetch failed"));
        assertThrows(IllegalArgumentException.class, () -> reportService.fetchReport(1, 10));
    }
}


```



<br>


## How to Run

# Prerequisites
Before running the project, ensure you have the following installed:

- Java 17
- Maven 3.6+ (optional you can use `./mvnw` command in place of `mvn` if maven is not already installed in your machine)
- PostgreSQL (or update the database configuration for your preferred database)
- google account (to download the project zip)

***Step 1: Clone the Repository***

```plaintext

download the project zip from google drive link 
googleLink: 
```
***Step 2: Configure the Database***

Update the src/main/resources/application.yml file with your PostgreSQL credentials and create report_db database:

```yml

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/report_db
    username: your_db_username
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

If PostgreSQL is not installed, update the configuration for H2 (an in-memory database for testing).
```

***Step 3: Build the Project***
Run the following command to build the project:

```sh
mvn clean install
```
***Step 4: Run the Application***

Start the Spring Boot application using:

```sh
./mvnw spring-boot:run

or

java -jar target/report-service-0.0.1-SNAPSHOT.jar
```

***Step 5: Access the API***

Once the application is running, use the following:

Health Check: http://localhost:8080/actuator/health

***Step 6: Testing the API***
Use Postman or cURL to interact with the API endpoints:

# Save Report
```sh

curl -X POST "http://localhost:8080/api/v1.0/reports/save" \
     -H "Content-Type: application/json" \
     -d '{"batchNo": 12345, "status": "ACTIVE", "createdAt": "2025-03-19T10:00:00"}'
```

# Fetch Reports
```sh

curl -X GET "http://localhost:8080/api/v1.0/reports?pageNo=1&pageSize=10"
```

***Update Report Status***
```sh

curl -X PUT "http://localhost:8080/api/v1.0/reports/update-status" \
     -H "Content-Type: application/json" \
     -d '{"batchIds": [12345], "status": "INACTIVE"}'
     
```

***Step 7: Running Tests***
To run all unit and integration tests, use:

```sh

./mvnw test -Dtest='Report*', GlobalExceptionHandlerTest

```

### **Time and Space Complexity Analysis (Including Controller Layer Calls)**

Each request is processed through the **controller layer** before reaching the **service layer**. The controller layer mainly adds a small **O(1)** overhead (validation, response wrapping), so the complexity is determined mostly by the **service layer**.

---

### **1. `saveReport(ReportRequestDto reportRequestDto)`**
- **Controller Layer Complexity**:
    - Request validation (`@Valid`) → **O(1)**
    - Calls `reportService.saveReport()` → **O(1)**
    - Wraps response in `ResponseEntity.ok()` → **O(1)**
- **Service Layer Complexity**:
    - Converts DTO to `ReportModel` → **O(1)**
    - Saves to database (`reportRepository.save()`) → **O(1)**
- **Overall Complexity**:
    - **Time Complexity**: **O(1)**
    - **Space Complexity**: **O(1)** (only a single object stored)

---

### **2. `deleteReport(long batchNo)`**
- **Controller Layer Complexity**:
    - Calls `reportService.deleteReport(batchNo)` → **O(1)**
    - Wraps response in `ResponseEntity.ok()` → **O(1)**
- **Service Layer Complexity**:
    - Retrieves report by `batchNo` (`reportRepository.getReportModelByBatchNo(batchNo)`) → **O(1)**
    - Deletes the report (`reportRepository.delete(reportModel)`) → **O(1)**
- **Overall Complexity**:
    - **Time Complexity**: **O(1)**
    - **Space Complexity**: **O(1)** (only single report reference stored)

---

### **3. `updateReportStatus(ReportUpdateDto reportUpdateDto)`**
- **Controller Layer Complexity**:
    - Validates request (`@Valid`) → **O(1)**
    - Calls `reportService.updateReportStatus(reportUpdateDto)` → **O(n)**
    - Wraps response in `ResponseEntity.ok()` → **O(1)**
- **Service Layer Complexity**:
    - Retrieves reports for `batchIds` (`reportRepository.fetchReportModelList(batchIds)`) → **O(n)**
    - Updates statuses (`stream().peek()`) → **O(n)**
    - Saves updated list (`reportRepository.saveAll()`) → **O(n)**
- **Overall Complexity**:
    - **Time Complexity**: **O(n)**
    - **Space Complexity**: **O(n)** (list of reports stored)

---

### **4. `fetchReport(int pageNo, int pageSize)`**
- **Controller Layer Complexity**:
    - Calls `reportService.fetchReport(pageNo, pageSize)` → **O(log n)**
    - Wraps response in `ResponseEntity.ok()` → **O(1)**
- **Service Layer Complexity**:
    - Constructs pageable query (`PageRequest.of()`) → **O(1)**
    - Fetches paginated data (`reportRepository.getAllByReport(pageable)`) → **O(log n)**
- **Overall Complexity**:
    - **Time Complexity**: **O(log n)**
    - **Space Complexity**: **O(k)** (depends on page size `k`)

---

### **Final Complexity Summary (Including Controller Layer)**
| Method | Controller Time | Service Time | Total Time Complexity | Total Space Complexity |
|---------|----------------|-------------|----------------------|----------------------|
| `saveReport()` | O(1) | O(1) | **O(1)** | **O(1)** |
| `deleteReport()` | O(1) | O(1) | **O(1)** | **O(1)** |
| `updateReportStatus()` | O(1) | O(n) | **O(n)** | **O(n)** |
| `fetchReport()` | O(1) | O(log n) | **O(log n)** | **O(k)** |

---

### **Key Takeaways:**
1. **Controller layer has minimal overhead (`O(1)`)**, mostly handling request validation and response wrapping.
2. **CRUD operations (`saveReport` and `deleteReport`) remain constant time (`O(1)`)**.
3. **Batch updates (`updateReportStatus`) have linear time complexity (`O(n)`)** due to fetching and modifying multiple reports.
4. **Paginated fetching (`fetchReport`) is efficient (`O(log n)`)**, leveraging database indexing.


### **Conclusion**

This Spring Boot project provides a well-structured and efficient system for managing reports, with a **clear separation of concerns** across the controller, service, and persistence layers. The **controller layer** ensures proper request handling and validation, while the **service layer** encapsulates business logic and interacts with the **repository layer** for database operations.

The system is optimized for **scalability and maintainability**, with most operations performing in **O(1) or O(n) complexity**, ensuring fast execution even as data grows. Features such as **batch updates and paginated fetching** make the application well-suited for handling large datasets efficiently.

Overall, this project is a **robust, modular, and extensible** solution for managing report records, following **best practices in Spring Boot development** while maintaining **high performance and reliability**. 🚀

`4. Code Coverage Screenshoot`
1st Test Iteration: https://drive.google.com/file/d/1Ub9BSYY0WwTKGof8RIarCc1gt0_uyCb9/view?usp=sharing
2nd Test Iteration: https://drive.google.com/file/d/1PQRMB5NsntuQ0TB50VAHeI1Qeg2_WGIq/view?usp=drive_link

code link: link-here