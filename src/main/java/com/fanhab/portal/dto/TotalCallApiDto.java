package com.fanhab.portal.dto;

import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalCallApiDto {
    private Long totalCallApiId;
    private Long apiId;
    private ApiStatusEnum apiStatus;
    private Integer totalApiCount;
    private Long contractId;
    private Long companyId;


}
