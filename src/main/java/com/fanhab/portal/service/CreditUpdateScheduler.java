package com.fanhab.portal.service;


import com.fanhab.portal.dto.enums.ApiCountSourceEnum;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.mapper.TotalApiCallMapper;
import com.fanhab.portal.portal.model.ContractDetailAPI;
import com.fanhab.portal.portal.model.TotalApiCall;
import com.fanhab.portal.portal.repository.ApiCatalogRepository;
import com.fanhab.portal.wso2.dto.ApiRequestDto;
import com.fanhab.portal.wso2.repository.ApiRequestsTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.fanhab.portal.utils.DateUtils.convertLocalDateTimeToTimestamp;
import static com.fanhab.portal.utils.DateUtils.convertTimestampToLong;

@Service
public class CreditUpdateScheduler {
    @Autowired
    private ApiRequestsTestRepository apiRequestRepository;
    @Autowired
    private TotalApiCallMapper totalApiCallMapper;
    @Autowired
    private TotalApiCallService totalApiCallService;

    @Scheduled(cron = "0 0 * * * *")
    public void updateRemainingCredit() {

        LocalDateTime endDateTime = LocalDateTime.now().minusSeconds(1);
        LocalDateTime startDateTime = endDateTime.minusHours(1);
        Long startDateL = convertTimestampToLong(convertLocalDateTimeToTimestamp(startDateTime))*1000;
        Long endDateL =(convertTimestampToLong(convertLocalDateTimeToTimestamp(endDateTime))*1000 )+ 999;
        List<ApiRequestDto> apiRequestDtos = apiRequestRepository.findApiRequestsGrouped(startDateL,endDateL)
                .stream()
                .map(totalApiCallMapper::mapObjectApiRequestDto)
                .collect(Collectors.toList());
        List<ContractDetailAPI> contractDetails = totalApiCallService.getActiveContractDetails();

        for (ContractDetailAPI contractDetail : contractDetails) {
            ApiRequestDto apiRequest = apiRequestDtos.stream()
                    .filter(apiRequestDto ->
                            apiRequestDto.getApiName().equals(contractDetail.getApiCatalog().getApiName()) &&
                                    apiRequestDto.getApplicationOwner().equals(contractDetail.getContract().getCompany().getCompanyName()) &&
                                    totalApiCallService.getApiStatusByResponseCode(apiRequestDto.getResponseCode()).equals(contractDetail.getApiStatus())
                    )
                    .findFirst()
                    .orElse(null);


            if (apiRequest == null) continue;


            Long contractId = contractDetail.getContractId();
            Long apiId = contractDetail.getApiId();
            ApiStatusEnum apiStatus = totalApiCallService.getApiStatusByResponseCode(apiRequest.getResponseCode());
            Long totalCount = apiRequest.getTotalCount();

            Long totalAmount = totalCount * contractDetail.getPrice();

        }
    }
}
