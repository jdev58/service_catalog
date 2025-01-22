package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.ProviderEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
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
@Table(name = "prt_contract_api")
public class ContractAPI extends BaseDomain {
    @Column(name = "CONTRACT_ID")
    Long contractId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACT_REF"))
    Contract contract;


    @Column(name = "API_ID")
    Long apiId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "API_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_API_CATALOG"))
    ApiCatalog apiCatalog;


    @Column(name = "ACTIVATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime activateDate;


    @Column(name = "API_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;


}
