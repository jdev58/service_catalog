package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.portal.model.Billing;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillingMapper {
    @Autowired
    private BillingDetailMapper billingDetailMapper;
    public BillingDto mapEntityToDto(Billing billing, List<BillingDetail> billingDetails) {
        List<BillingDetailDto> billingDetailDtoList = billingDetails.stream()
                .map(billingDetailMapper::mapEntityToDto)
                .collect(Collectors.toList());
        return new BillingDto(
                billing.getCompanyId(),
                billing.getContractId(),
                billing.getDiscount(),
                billing.getBillStatus().name(),
                billing.getTotalAmount(),
                billingDetailDtoList
        );
    }
}
