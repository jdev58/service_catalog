package com.fanhab.portal.dto.request;

import com.fanhab.portal.dto.enums.RateCalculationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillingDto {
    private Long companyId;
    private Long contractId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private RateCalculationState rateCalculationState;
}
