package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.stereotype.Component;

@Component
public class BillingDetailMapper {
    public BillingDetailDto mapEntityToDto(BillingDetail billingDetail, Integer totalApiCall,Integer perPrice){
        BillingDetailDto billingDetailDto = new BillingDetailDto(
                billingDetail.getBillingId(),
                billingDetail.getApiId(),
                billingDetail.getApi().getApiName(),
                billingDetail.getApiResponseCode().name(),
                totalApiCall,
                billingDetail.getApiTotalAmount().longValue(),
                perPrice
        );
        return  billingDetailDto;
    }

}
