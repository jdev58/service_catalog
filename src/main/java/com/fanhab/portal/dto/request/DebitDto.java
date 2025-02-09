package com.fanhab.portal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DebitDto {
    private String userName;
    private Long amount;
    private String referalCode;
    private String trnDescription;
}
