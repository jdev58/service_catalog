package com.fanhab.portal.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestDto {
    private String applicationOwner;
    private String apiName;
    private Integer responseCode;
    private Long totalCount;
}
