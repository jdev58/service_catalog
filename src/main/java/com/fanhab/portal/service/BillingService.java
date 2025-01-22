package com.fanhab.portal.service;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.BillStatus;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.mapper.BillingMapper;
import com.fanhab.portal.model.Billing;
import com.fanhab.portal.model.BillingDetail;
import com.fanhab.portal.model.ContractDetailAPI;
import com.fanhab.portal.model.TotalApiCall;
import com.fanhab.portal.repository.BillingDetailRepository;
import com.fanhab.portal.repository.BillingRepository;
import com.fanhab.portal.repository.ContractDetailApiRepository;
import com.fanhab.portal.repository.TotalApiCallRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillingService {
    @Autowired
    private TotalApiCallRepository totalApiCallRepository;
    @Autowired
    private ContractDetailApiRepository contractDetailApiRepository;
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private BillingDetailRepository billingDetailRepository;
    @Autowired
    private BillingMapper billingMapper;
    @Transactional
    public void createBillingAndDetailsForNotCalculatedApiCalls() {
        List<TotalCallApiDto> apiCalls = totalApiCallRepository.findNotCalculatedTotalApiCalls();
        Map<Long, Map<Long, List<TotalCallApiDto>>> groupedData = apiCalls.stream()
                .collect(Collectors.groupingBy(
                        TotalCallApiDto::getCompanyId,
                        Collectors.groupingBy(TotalCallApiDto::getContractId)
                ));

        groupedData.forEach((companyId, contractMap) ->
                contractMap.forEach((contractId, callList) -> {
                    Billing billing = new Billing();
                    billing.setCompanyId(companyId);
                    billing.setContractId(contractId);
                    billing.setDiscount(0L); // or any logic for discount
                    billing.setTotalAmount(calcBillingTotalAmount(callList,contractId));
                    billing.setBillStatus(BillStatus.READY);
                    Billing savedBilling = billingRepository.save(billing);
                    // create billingDetail for each TotalApiCall
                    callList.forEach(apiCall -> {
                        BillingDetail detail = new BillingDetail();
                        detail.setBillingId(savedBilling.getId());
                        detail.setApiCall(apiCall.getTotalCallApiId());
                        detail.setApiTotalAmount(calcTotalAmount(contractId,apiCall));
                        billingDetailRepository.save(detail);
                        setApiCallAsCalculated(apiCall);
                    });
                })
        );


    }



    private Double calcBillingTotalAmount(List<TotalCallApiDto> callList, Long contractId) {
        return callList.stream()
                .mapToDouble(apiCall -> calcTotalAmount(contractId, apiCall))
                .sum();
    }
    private Double calcTotalAmount(Long contractId,TotalCallApiDto totalCallApiDto){
        Integer totalCall = sumTotalCall(totalCallApiDto);
        List<ContractDetailAPI> contractDetails = getContractDetails(
                totalCallApiDto.getContractId(),
                totalCallApiDto.getApiId(),
                totalCallApiDto.getApiStatus());
        for (ContractDetailAPI contractDetailAPI:contractDetails){
            if(isWithinCallRange(totalCall,contractDetailAPI))
                return Double.valueOf(totalCallApiDto.getTotalApiCount()*contractDetailAPI.getPrice());
        }
        return 0D;
    }
    private List<ContractDetailAPI> getContractDetails(Long contractId, Long apiId, ApiStatusEnum apiStatus) {
        List<ContractDetailAPI> contractDetails = contractDetailApiRepository.findByContractIdAndApiIdAndApiStatus(contractId, apiId,apiStatus);
        return contractDetails;
    }


    private boolean isWithinCallRange(Integer totalCallSum, ContractDetailAPI contractDetail) {
        return totalCallSum >= contractDetail.getMinCallNo() && totalCallSum <= contractDetail.getMaxCallNo();
    }
    private Integer sumTotalCall(TotalCallApiDto totalCallApiDto){
        List<TotalApiCall> apiCalls = totalApiCallRepository.findByApiIdAndContractIdAndApiStatusAndProcessState(
                totalCallApiDto.getApiId(),
                totalCallApiDto.getContractId(),
                totalCallApiDto.getApiStatus(),
                ProcessStatusEnum.CALCULATED
        );
        Integer totalCallCountSum = (apiCalls.stream()
                .mapToInt(TotalApiCall::getTotalApiCallCount)
                .sum())+totalCallApiDto.getTotalApiCount();
        return totalCallCountSum;
    }


    private void setApiCallAsCalculated(TotalCallApiDto apiDto) {
        TotalApiCall apiCall = totalApiCallRepository.findById(apiDto.getTotalCallApiId())
                .orElseThrow(() -> new EntityNotFoundException("TotalApiCall not found for ID: " + apiDto.getTotalCallApiId()));
        apiCall.setProcessState(ProcessStatusEnum.CALCULATED);
        totalApiCallRepository.save(apiCall);
    }



}
