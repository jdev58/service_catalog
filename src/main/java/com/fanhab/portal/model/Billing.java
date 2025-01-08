package com.fanhab.portal.model;


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
@Table(name = "prt_billing")
public class Billing extends BaseDomain {
    @Column(name = "COMPANY_ID")
    Long companyId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILLING_COMPANY"))
    Company company;



    @Column(name = "CONTRACT_ID")
    Long contractId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILLING_CONTRACT"))
    Contract contract;


/*
    @Column(name = "USER_ID")
    Long userId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILLING_CONTRACT"))
    User contract;*/






}
