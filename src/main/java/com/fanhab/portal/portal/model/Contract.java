package com.fanhab.portal.portal.model;


import com.fanhab.portal.dto.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "svc_contract")
public class Contract extends BaseDomain {
    @Column(name = "COMPANY_ID")
    Long companyId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACT_COMPANY"))
    Company company;



    @Column(name = "CONTRACT_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum contractStatus;

    @Column(name = "CONTRACT_NUMBER",nullable = false)
    private String contractNumber;

    @Column(name = "START_DATE",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE",nullable = false)
    private LocalDateTime endDate;

}
