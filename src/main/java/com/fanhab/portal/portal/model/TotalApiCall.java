package com.fanhab.portal.portal.model;


import com.fanhab.portal.dto.enums.ApiCountSourceEnum;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "svc_total_api_call")
public class TotalApiCall extends BaseDomain {
    @Column(name = "CONTRACT_ID")
    Long contractId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_TOTALAPI_CONTRACT"))
    Contract contract;



    @Column(name = "API_ID")
    Long apiId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "API_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_TOTALAPI_API"))
    Api apiCatalog;


    @Column(name = "TOTAL_API_COUNT")
    Integer totalApiCallCount;

    @Column(name = "API_STATUS")
    @Enumerated(EnumType.STRING)
    ApiStatusEnum apiStatus;

    @Column(name = "PROCESS_STATE")
    @Enumerated(EnumType.STRING)
    ProcessStatusEnum processState;

    @Column(name = "COUNT_SOURCE")
    @Enumerated(EnumType.STRING)
    ApiCountSourceEnum apiCountSource;

//    @Column(name = "TOTAL_API_CALL_DATE")
//    LocalDate totalApiCallDate;

    @Column(name = "API_CALL_FROM_DATE")
    LocalDateTime fromDate;

    @Column(name = "API_CALL_TO_DATE")
    LocalDateTime toDate;

}
