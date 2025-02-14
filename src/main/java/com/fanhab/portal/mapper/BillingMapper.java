package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.request.DebitDto;
import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.portal.model.Billing;
import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.fanhab.portal.utils.DateUtils.convertLocalDateTimeToTimestamp;
import static com.fanhab.portal.utils.DateUtils.convertTimestampToLong;

@Component
public class BillingMapper {
    @Autowired
    private BillingDetailMapper billingDetailMapper;
    public BillingDto mapEntityToDto(Billing billing, List<BillingDetailDto> billingDetailDtoList) {
//        List<BillingDetailDto> billingDetailDtoList = billingDetails.stream()
//                .map(billingDetailMapper::mapEntityToDto)
//                .collect(Collectors.toList());
        return new BillingDto(
                billing.getId(),
                billing.getCompanyId(),
                billing.getContractId(),
                billing.getCompany().getCompanyName(),
                billing.getCompany().getCompanyPersianName(),
                billing.getContract().getContractNumber(),
                convertTimestampToLong(convertLocalDateTimeToTimestamp(billing.getContract().getStartDate())),
                convertTimestampToLong(convertLocalDateTimeToTimestamp(billing.getContract().getEndDate())),
                billing.getDiscount(),
                billing.getBillStatus().name(),
                billing.getTotalAmount().longValue(),
                billingDetailDtoList
        );
    }

    public DebitDto mapEntityToDebitDto(Billing billing){
        DebitDto debitDto = new DebitDto(
                billing.getCompany().getCompanyName(),
                billing.getTotalAmount().longValue(),
                billing.getId().toString(),
                "debit  account " + billing.getId().toString()
        );
        return debitDto;
    }
    public BillingDto mapBillingEntityToDto(Billing billing){
        BillingDto billingDto = new BillingDto();
        billingDto.setCompanyId(billing.getCompanyId());
        billingDto.setContractId(billing.getContractId());
        billingDto.setBillingId(billing.getId());
        billingDto.setCompanyName(billing.getCompany().getCompanyName());
        billingDto.setCompanyPersianName(billing.getCompany().getCompanyPersianName());
        billingDto.setBillStatus(billing.getBillStatus().name());
        billingDto.setContractNo(billing.getContract().getContractNumber());
        return billingDto;
    }
}
