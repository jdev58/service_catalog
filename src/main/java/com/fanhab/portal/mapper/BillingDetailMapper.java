package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.stereotype.Component;

@Component
public class BillingDetailMapper {
    public BillingDetailDto mapEntityToDto(BillingDetail billingDetail){
        BillingDetailDto billingDetailDto = new BillingDetailDto(
                billingDetail.getId(),
                billingDetail.getApiId(),
                billingDetail.getApi().getApiName(),
                billingDetail.getApi().getApiPersianName(),
                billingDetail.getApi().getApiCode(),
                billingDetail.getApiResponseCode().name(),
                billingDetail.getTotalApiCallCount(),
                billingDetail.getApiTotalAmount().longValue(),
                billingDetail.getPrice()
        );
        return  billingDetailDto;
    }

}
