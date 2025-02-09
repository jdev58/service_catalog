package com.fanhab.portal.dto;

import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalCallApiDto {
    private Long totalCallApiId;
    private Long apiId;
    private Long contractId;
    private ApiStatusEnum apiStatus;
    private Integer totalApiCount;
    private Integer perPrice;
    private Double totalAmount;
    private Long companyId;
}
