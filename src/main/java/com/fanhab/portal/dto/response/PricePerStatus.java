package com.fanhab.portal.dto.response;

import com.fanhab.portal.dto.enums.ApiStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PricePerStatus {
    private ApiStatusEnum apiStatusEnum;
    private Integer price;
}
