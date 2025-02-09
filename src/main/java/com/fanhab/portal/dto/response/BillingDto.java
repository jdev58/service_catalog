package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingDto {
    private Long companyId;
    private Long contractId;
    private String companyName;
    private String contractNo;
    private Timestamp contractStartDate;
    private Timestamp contractEndDate;
    private Long Discount;
    private String BillStatus;
    private Long totalAmount;
    List<BillingDetailDto> billingDetailDtoList;
}
