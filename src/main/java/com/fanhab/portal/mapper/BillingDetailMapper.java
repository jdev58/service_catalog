package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.stereotype.Component;

@Component
public class BillingDetailMapper {
    public BillingDetailDto mapEntityToDto(BillingDetail billingDetail){
        BillingDetailDto billingDetailDto = new BillingDetailDto(
                billingDetail.getBillingId(),
                billingDetail.getApiId(),
                billingDetail.getApi().getApiName(),
                billingDetail.getApiResponseCode().name(),
                billingDetail.getApiTotalAmount().longValue()
        );
        return  billingDetailDto;
    }

}
