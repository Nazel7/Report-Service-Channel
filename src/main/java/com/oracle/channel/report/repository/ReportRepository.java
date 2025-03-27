package com.oracle.channel.report.repository;

import com.oracle.channel.report.enums.ReportStatus;
import com.oracle.channel.report.models.ReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing ReportModel entities.
 * Provides methods for querying and updating reports in the database.
 *
 * @author GafarOlanipekun
 * @since 19/03/2025
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportModel, UUID> {

    /**
     * Retrieves a ReportModel by its batch number.
     *
     * @param batchNo the batch number of the report
     * @return the corresponding ReportModel, or null if not found
     */
    ReportModel getReportModelByBatchNo(long batchNo);

    /**
     * Fetches a paginated list of active reports ordered by their last update timestamp.
     *
     * @param pageable pagination and sorting parameters
     * @return a page of active reports
     */
    @Query("SELECT r from ReportModel r where r.status = 'ACTIVE' order by r.updatedAt desc")
    Page<ReportModel> getAllByReport(Pageable pageable);

    /**
     * Retrieves a list of reports based on the provided batch numbers.
     *
     * @param batchIds the list of batch numbers to fetch reports for
     * @return a list of matching ReportModel instances
     */
    @Query("SELECT r from ReportModel r where r.batchNo in ?1")
    List<ReportModel> fetchReportModelList(List<Long> batchIds);
}