package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ContractInfoDto {
    private Long contractId;
    private String contractNo;
    private String companyName;
    List<ApiDto> apiDtoList;
}
