package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.model.BillingDetail;
import org.springframework.stereotype.Component;

@Component
public class BillingDetailMapper {
    public BillingDetailDto mapEntityToDto(BillingDetail billingDetail){
        BillingDetailDto billingDetailDto = new BillingDetailDto(
                billingDetail.getBillingId(),
                billingDetail.getApiCall(),
                billingDetail.getApiTotalAmount()
        );
        return  billingDetailDto;
    }

}
