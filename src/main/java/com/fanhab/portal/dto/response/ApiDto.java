package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ApiDto {
    private String apiCode;
    private String apiName;
    private List<PricePerStatus> pricePerStatuses = new ArrayList<>();
}
