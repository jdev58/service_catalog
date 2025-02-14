package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.stereotype.Component;

@Component
public class BillingDetailMapper {
    public BillingDetailDto mapEntityToDto(BillingDetail billingDetail, Integer totalApiCall,Integer perPrice){
        BillingDetailDto billingDetailDto = new BillingDetailDto(
                billingDetail.getId(),
                billingDetail.getApiId(),
                billingDetail.getApi().getApiName(),
                billingDetail.getApi().getApiPersianName(),
                billingDetail.getApi().getApiCode(),
                billingDetail.getApiResponseCode().name(),
                totalApiCall,
                billingDetail.getApiTotalAmount().longValue(),
                perPrice
        );
        return  billingDetailDto;
    }

}
