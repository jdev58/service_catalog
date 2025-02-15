package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NonNull
public class CompanyDto {
    private Long companyId;
    private String companyName;
    private String companyPersianName;
}
