package com.fanhab.portal.service;

import com.fanhab.portal.mapper.TotalApiCallMapper;
import com.fanhab.portal.portal.model.ContractDetailAPI;
import com.fanhab.portal.portal.repository.ContractDetailApiRepository;
import com.fanhab.portal.wso2.dto.ApiRequestDto;
import com.fanhab.portal.dto.enums.ApiCountSourceEnum;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.model.ContractAPI;
import com.fanhab.portal.portal.model.TotalApiCall;
import com.fanhab.portal.wso2.repository.ApiRequestsTestRepository;
import com.fanhab.portal.portal.repository.ContractApiRepository;
import com.fanhab.portal.portal.repository.TotalApiCallRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fanhab.portal.utils.DateUtils.*;

@Service
public class TotalApiCallService {

    @Autowired
    private ApiRequestsTestRepository apiRequestRepository;
    @Autowired
    private ContractApiRepository contractApiRepository;
    @Autowired
    private TotalApiCallRepository totalApiCallRepository;
    @Autowired
    private TotalApiCallMapper totalApiCallMapper;
    @Autowired
    private ContractDetailApiRepository contractDetailApiRepository;

    public void createTotalapiCall(CreateBillingDto createBillingDto){
        LocalDateTime startDate = createBillingDto.getFromDate().atStartOfDay();
        LocalDateTime endDate = createBillingDto.getToDate().atTime(23, 59, 59);
        List<TotalApiCall> exsistingTotalApiCall = totalApiCallRepository.findExistingTotalApiCall(startDate, endDate);
        List<LocalDate> allDays = createBillingDto.getFromDate().datesUntil(createBillingDto.getToDate().plusDays(1)).collect(Collectors.toList());
        List<LocalDate> missingDays = allDays.stream()
                .filter(day -> exsistingTotalApiCall.stream().noneMatch(log -> log.getFromDate().equals(day.atStartOfDay())
                && log.getToDate().equals(day.atTime(23, 59, 59))))
                .collect(Collectors.toList());
        for(LocalDate day : missingDays){

        }
    }

    private void createTotalApiCall(List<ApiRequestDto> apiRequestDtos,List<ContractDetailAPI> contractDetails,LocalDateTime startDate,LocalDateTime endDate){
        List<TotalApiCall> totalApiCalls = new ArrayList<>();
        for (ContractDetailAPI contractDetail : contractDetails) {
            ApiRequestDto apiRequest = apiRequestDtos.stream()
                    .filter(apiRequestDto ->
                            apiRequestDto.getApiName().equals(contractDetail.getApiCatalog().getApiName()) &&
                                    apiRequestDto.getApplicationOwner().equals(contractDetail.getContract().getCompany().getCompanyName()) &&
                                    getApiStatusByResponseCode(apiRequestDto.getResponseCode()).equals(contractDetail.getApiStatus())
                    )
                    .findFirst()
                    .orElse(null);


            if (apiRequest == null) continue;


            Long contractId = contractDetail.getContractId();
            Long apiId = contractDetail.getApiId();
            ApiStatusEnum apiStatus = getApiStatusByResponseCode(apiRequest.getResponseCode());
            Long totalCount = apiRequest.getTotalCount();

            TotalApiCall totalApiCall = new TotalApiCall();
            totalApiCall.setContractId(contractId);
            totalApiCall.setApiId(apiId);
            totalApiCall.setTotalApiCallCount(totalCount.intValue());
            totalApiCall.setApiStatus(apiStatus);
            totalApiCall.setProcessState(ProcessStatusEnum.NOT_CALCULATED);
            totalApiCall.setApiCountSource(ApiCountSourceEnum.WSO2);
            totalApiCall.setFromDate(startDate);
            totalApiCall.setToDate(endDate);

            totalApiCalls.add(totalApiCall);
        }



        if (!totalApiCalls.isEmpty()) {
            saveAllTotalApi(totalApiCalls);
        }
    }

//    private void createTotalApiCall(List<ApiRequestDto> apiRequestDtos,List<Map<String, Object>> contractDetails,LocalDateTime startDate,LocalDateTime endDate){
//        Map<String, Map<String, Map<String, Long>>> contractApiMap = new HashMap<>();
//        for (Map<String, Object> contractDetail : contractDetails) {
//            Map<String, Object> companyMap = (Map<String, Object>) contractDetail.get("company");
//            Map<String, Object> apiMap = (Map<String, Object>) contractDetail.get("api");
//            Map<String, Object> contractMap = (Map<String, Object>) contractDetail.get("contract");
//
//            String companyName = (String) companyMap.get("name");
//            String apiName = (String) apiMap.get("name");
//            Long contractId = (Long) contractMap.get("id");
//            Long apiId = (Long) apiMap.get("id");
//
//            Map<String, Long> apiInfo = new HashMap<>();
//            apiInfo.put("contractId", contractId);
//            apiInfo.put("apiId", apiId);
//
//            contractApiMap
//                    .computeIfAbsent(companyName, k -> new HashMap<>())
//                    .put(apiName, apiInfo);
//        }
//
//        List<TotalApiCall> totalApiCalls = new ArrayList<>();
//        for (ApiRequestDto apiRequest : apiRequestDtos) {
//            String companyName = apiRequest.getApplicationOwner();
//            String apiName = apiRequest.getApiName();
//            Long totalCount = apiRequest.getTotalCount();
//
//            Map<String, Long> apiInfo = contractApiMap.getOrDefault(companyName, new HashMap<>()).get(apiName);
//            if (apiInfo == null) continue;
//
//            Long contractId = apiInfo.get("contractId");
//            Long apiId = apiInfo.get("apiId");
//            ApiStatusEnum apiStatus = getApiStatusByResponseCode(apiRequest.getResponseCode());
//
//            TotalApiCall totalApiCall = new TotalApiCall();
//            totalApiCall.setContractId(contractId);
//            totalApiCall.setApiId(apiId);
//            totalApiCall.setTotalApiCallCount(totalCount.intValue());
//            totalApiCall.setApiStatus(apiStatus);
//            totalApiCall.setProcessState(ProcessStatusEnum.NOT_CALCULATED);
//            totalApiCall.setApiCountSource(ApiCountSourceEnum.WSO2);
//            totalApiCall.setFromDate(startDate);
//            totalApiCall.setToDate(endDate);
//
//            totalApiCalls.add(totalApiCall);
//
//        }
//        if (!totalApiCalls.isEmpty()) {
//            saveAllTotalApi(totalApiCalls);
//        }
//    }
    @Transactional
    private void saveAllTotalApi(List<TotalApiCall> totalApiCalls){
        totalApiCallRepository.saveAll(totalApiCalls);
    }

    public ApiStatusEnum getApiStatusByResponseCode(Integer responseCode) {
        if (responseCode == 200) {
            return ApiStatusEnum.STATE_200;
        }
        else  {
            return ApiStatusEnum.STATE_231;
        }
    }

//    public List<Map<String, Object>> getContractDetails() {
//        List<ContractAPI> contractAPIs = contractApiRepository.findByStatusEnum(StatusEnum.ACTIVE);
//        return contractAPIs.stream().map(contractAPI -> {
//            Map<String, Object> result = new HashMap<>();
//            Contract contract = contractAPI.getContract();
//            result.put("contract", Map.of("id", contract.getId()));
//            result.put("company", Map.of(
//                    "id", contract.getCompany().getId(),
//                    "name", contract.getCompany().getCompanyName()
//            ));
//            result.put("api", Map.of(
//                    "id", contractAPI.getApiCatalog().getId(),
//                    "name", contractAPI.getApiCatalog().getApiName()
//            ));
//            return result;
//        }).collect(Collectors.toList());
//    }
    public List<ContractDetailAPI> getActiveContractDetails() {
        return contractDetailApiRepository.findByActiveContract(StatusEnum.ACTIVE);
    }
    private boolean hasTotalApiCallsWithinTimeRange(LocalDateTime endDate) {
        LocalDateTime totalCallEndDate = totalApiCallRepository.findMaxToDate();
        if (totalCallEndDate != null && totalCallEndDate.isAfter(endDate)) {
            return false;
        }
        else
            return true;
    }

}
