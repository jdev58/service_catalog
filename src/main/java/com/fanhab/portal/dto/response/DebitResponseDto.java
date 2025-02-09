package com.fanhab.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DebitResponseDto {
    private String accountId;
    private String currencyEnum;
    private String userName;
    private String balance;
    private String lstTransaction;
}
