package com.fanhab.portal.portal.model;

import com.fanhab.portal.dto.enums.ApiStatusEnum;
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
@Table(name = "svc_billing_detail")
public class BillingDetail extends BaseDomain {


    @Column(name = "BILLING_ID")
    Long billingId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "BILLING_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILLING_DETAIL_BILL"))
    Billing billing;


//    @Column(name = "API_CALL_ID")
//    Long apiCall;
//
//    @ManyToOne(fetch= FetchType.EAGER)
//    @JoinColumn(name = "API_CALL_ID", referencedColumnName = "ID",
//            insertable = false, updatable = false,
//            foreignKey = @ForeignKey(name = "FK_BILL_API_CALL"))
//    TotalApiCall totalApiCALL;

    @Column(name = "API_ID")
    Long apiId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "API_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_BILL_DETAIL_API"))
    Api api;

    @Column(name = "API_TOTAL_AMOUNT")
    Double apiTotalAmount;

    @Column(name = "API_RESPONSE_CODE")
    @Enumerated(EnumType.STRING)
    ApiStatusEnum apiResponseCode;

    @Column(name = "TOTAL_API_COUNT")
    Integer totalApiCallCount;

    @Column(name = "per_call_price")
    Integer price;
}
