package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ContractInfoDto {
    private Long contractId;
    private String contractNo;
    private String companyName;
    private Timestamp contractStartDate;
    private Timestamp contractEndDate;
    private List<ApiDto> apiDtoList = new ArrayList<>();
}
