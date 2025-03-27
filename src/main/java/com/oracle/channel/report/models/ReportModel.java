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
    @Column(length = 48)
    private Date createdAt;

    @UpdateTimestamp
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
