package com.fanhab.portal.model;

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
@Table(name = "prt_billing_detail")
public class BillingDetail extends BaseDomain {


    @Column(name = "BILLING_ID")
    Long billingId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "BILLING_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILLING_DETAIL_BILL"))
    Billing billing;


    @Column(name = "API_CALL_ID")
    Long apiCall;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "API_CALL_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILL_API_CALL"))
    TotalApiCall totalApiCALL;

    @Column(name = "API_TOTAL_AMOUNT")
    Double apiTotalAmount;





}
