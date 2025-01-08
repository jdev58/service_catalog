package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.ApiCountSourceEnum;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "prt_api_call")
public class TotalApiCALL extends BaseDomain {
    @Column(name = "CONTRACT_ID")
    Long contractId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACT_API"))
    Contract contract;



    @Column(name = "API_ID")
    Long apiId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "API_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACT_API"))
    ApiCatalog apiCatalog;


    @Column(name = "TOTAL_API_COUNT")
    Integer totalApiCallCount;

    @Column(name = "API_STATUS")
    ApiStatusEnum apiStatus;

    @Column(name = "PROCESS_STATE")
    ProcessStatusEnum processState;

    @Column(name = "COUNT_SOURCE")
    ApiCountSourceEnum apiCountSource;





}
