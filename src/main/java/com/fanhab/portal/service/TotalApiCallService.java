package com.fanhab.portal.service;

import com.fanhab.portal.dto.ApiRequestDto;
import com.fanhab.portal.dto.enums.ApiCountSourceEnum;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.model.Contract;
import com.fanhab.portal.model.ContractAPI;
import com.fanhab.portal.model.TotalApiCall;
import com.fanhab.portal.repository.ApiRequestsTestRepository;
import com.fanhab.portal.repository.ContractApiRepository;
import com.fanhab.portal.repository.TotalApiCallRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TotalApiCallService {
    @Autowired
    private ApiRequestsTestRepository apiRequestRepository;
    @Autowired
    private ContractApiRepository contractApiRepository;
    @Autowired
    private TotalApiCallRepository totalApiCallRepository;

    public void createTotalapiCall(Long startDate,Long endDate){
        List<ApiRequestDto> apiRequestDtos = apiRequestRepository.findApiRequestsGrouped(startDate,endDate);
        List<Map<String, Object>> contractDetails = getContractDetails();

        createTotalApiCall(apiRequestDtos,contractDetails);

    }

    private void createTotalApiCall(List<ApiRequestDto> apiRequestDtos,List<Map<String, Object>> contractDetails){
        Map<String, Map<String, Long>> contractApiMap = new HashMap<>();
        for (Map<String, Object> contractDetail : contractDetails) {
            Map<String, Object> companyMap = (Map<String, Object>) contractDetail.get("company");
            Map<String, Object> apiMap = (Map<String, Object>) contractDetail.get("api");
            Map<String, Object> contractMap = (Map<String, Object>) contractDetail.get("contract");

            String companyName = (String) companyMap.get("name");
            String apiName = (String) apiMap.get("name");
            Long contractId = (Long) contractMap.get("id");
            Long apiId = (Long) apiMap.get("id");

            contractApiMap
                    .computeIfAbsent(companyName, k -> new HashMap<>())
                    .put(apiName, contractId);
        }

        List<TotalApiCall> totalApiCalls = new ArrayList<>();
        for (ApiRequestDto apiRequest : apiRequestDtos) {
            String companyName = apiRequest.getApplicationOwner();
            String apiName = apiRequest.getApiName();
            Long totalCount = apiRequest.getTotalCount();

            Long contractId = contractApiMap.getOrDefault(companyName, new HashMap<>()).get(apiName);
            if (contractId == null) continue;
            ApiStatusEnum apiStatus = getApiStatusByResponseCode(apiRequest.getResponseCode());

            TotalApiCall totalApiCall = new TotalApiCall();
            totalApiCall.setContractId(contractId);
            totalApiCall.setApiId(contractId); // مقدار apiId از contractId ذخیره شده است
            totalApiCall.setTotalApiCallCount(totalCount.intValue());
            totalApiCall.setApiStatus(apiStatus);
            totalApiCall.setProcessState(ProcessStatusEnum.NOT_CALCULATED);
            totalApiCall.setApiCountSource(ApiCountSourceEnum.WSO2);
//            totalApiCall.setFromDate(LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC));
//            totalApiCall.setToDate(LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC));

            totalApiCalls.add(totalApiCall);
            if (!totalApiCalls.isEmpty()) {
                saveAllTotalApi(totalApiCalls);
            }
        }
    }
    @Transactional
    private void saveAllTotalApi(List<TotalApiCall> totalApiCalls){
        totalApiCallRepository.saveAll(totalApiCalls);
    }
    private ApiStatusEnum getApiStatusByResponseCode(Integer responseCode) {
        if (responseCode == 200) {
            return ApiStatusEnum.STATE_200;
        }
        else  {
            return ApiStatusEnum.STATE_231;
        }
    }

    public List<Map<String, Object>> getContractDetails() {
        List<ContractAPI> contractAPIs = contractApiRepository.findByStatusEnum(StatusEnum.ACTIVE);
        return contractAPIs.stream().map(contractAPI -> {
            Map<String, Object> result = new HashMap<>();
            Contract contract = contractAPI.getContract();
            result.put("contract", Map.of("id", contract.getId()));
            result.put("company", Map.of(
                    "id", contract.getCompany().getId(),
                    "name", contract.getCompany().getCompanyName()
            ));
            result.put("api", Map.of(
                    "id", contractAPI.getApiCatalog().getId(),
                    "name", contractAPI.getApiCatalog().getApiName()
            ));
            return result;
        }).collect(Collectors.toList());
    }

}
