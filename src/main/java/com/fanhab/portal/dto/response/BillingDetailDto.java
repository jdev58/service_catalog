package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingDetailDto {
    private Long billingId;
    private Long apiId;
    private String apiName;
    private String apiStatus;
    private Integer totalApiCallCount;
    private Long apiTotalAmount;
    private Integer pricePerCall;

}
