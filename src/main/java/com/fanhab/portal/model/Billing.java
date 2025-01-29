package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.BillStatusEnum;
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
@Table(name = "svc_billing")
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




    @Column(name = "BILL_DISCOUNT")
    Long Discount;

    @Column(name = "BILL_TOTAL_AMOUNT")
    Double totalAmount;




    @Column(name = "BILL_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatusEnum billStatus;

    @Column(name = "BILL_FROM_DATE")
    LocalDateTime fromDate;

    @Column(name = "BILL_TO_DATE")
    LocalDateTime toDate;

}
