package com.fanhab.portal.service;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public List<BillingDto> createBillingAndDetailsForNotCalculatedApiCalls(CreateBillingDto createBillingDto) {
        int pageIndex = 0;
        int pageSize = 1000;
        List<BillingDto> billingDtoList = new ArrayList<>();
        while (true){
            Page<TotalCallApiDto> totalApiCallPage = fetchPagedTotalApiCall(createBillingDto, pageIndex, pageSize);
            if (totalApiCallPage.isEmpty()) {
                break;
            }
            Map<Long, List<TotalCallApiDto>> apiCallGroupedByContractId = groupedByContractId(totalApiCallPage.getContent());
            billingDtoList = createBillingAndBillingDetail(apiCallGroupedByContractId);
            setApiCallAsCalculated(totalApiCallPage.getContent());
            pageIndex++;
        }
        return billingDtoList;
    }

    private Page<TotalCallApiDto> fetchPagedTotalApiCall(CreateBillingDto createBillingDto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return totalApiCallRepository.findNotCalculatedTotalApiCalls(
                createBillingDto.getContractId(),
                createBillingDto.getFromDate().atStartOfDay(),
                createBillingDto.getToDate().atTime(23, 59, 59, 999999999),
                pageable
        );
    }

    private Map<Long, List<TotalCallApiDto>> groupedByContractId(List<TotalCallApiDto> totalCallApiDtoList) {
//        Map<Long, List<TotalCallApiDto>> groupedByContract = new HashMap<>();
        return totalCallApiDtoList.stream()
                .collect(Collectors.groupingBy(
                        TotalCallApiDto::getContractId,
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        dto -> dto.getApiId() + "_" + dto.getApiStatus(),
                                        Collectors.reducing((dto1, dto2) -> {
                                            dto1.setTotalApiCount(dto1.getTotalApiCount() + dto2.getTotalApiCount());
                                            dto1.setTotalAmount(dto1.getTotalAmount() + dto2.getTotalAmount());
                                            return dto1;
                                        })
                                ),
                                map -> map.values().stream()
                                        .map(Optional::get)
                                        .collect(Collectors.toList())
                        )
                ));

    }
    @Transactional
    private List<BillingDto> createBillingAndBillingDetail(Map<Long, List<TotalCallApiDto>> groupedApiCall){
        List<BillingDto> billingDtoList = new ArrayList<>();
        for (Map.Entry<Long, List<TotalCallApiDto>> entry : groupedApiCall.entrySet()) {
            Long contractId = entry.getKey();
            List<TotalCallApiDto> apiDtos = entry.getValue();

            Double totalAmount = apiDtos.stream()
                    .mapToDouble(TotalCallApiDto::getTotalAmount)
                    .sum();
            Long companyId = apiDtos.stream().map(TotalCallApiDto::getCompanyId).findFirst().get();

            Billing billing = new Billing();
            billing.setCompanyId(companyId);
            billing.setContractId(contractId);
            billing.setTotalAmount(totalAmount);
            billing.setBillStatus(BillStatusEnum.READY);

            billing = billingRepository.save(billing);

            List<BillingDetail> billingDetails = new ArrayList<>();
            for(TotalCallApiDto totalCallApiDto:apiDtos){
                BillingDetail billingDetail = new BillingDetail();
                billingDetail.setBillingId(billing.getId());
                billingDetail.setApiId(totalCallApiDto.getApiId());
                billingDetail.setApiResponseCode(totalCallApiDto.getApiStatus());
                billingDetail.setApiTotalAmount(totalCallApiDto.getTotalAmount());

                billingDetails.add(billingDetailRepository.save(billingDetail));
            }
            BillingDto billingDto = billingMapper.mapEntityToDto(billing, billingDetails);
            billingDtoList.add(billingDto);
        }
        return billingDtoList;
    }

    @Transactional
    private void setApiCallAsCalculated(List<TotalCallApiDto> apiDtos) {

        List<TotalApiCall> updatedCalls = apiDtos.stream()
                .map(apiDto -> {
                    TotalApiCall apiCall = totalApiCallRepository.findById(apiDto.getTotalCallApiId())
                            .orElseThrow(() -> new EntityNotFoundException("TotalApiCall not found for ID: " + apiDto.getTotalCallApiId()));
                    apiCall.setProcessState(ProcessStatusEnum.CALCULATED);
                    return apiCall;
                })
                .collect(Collectors.toList());

        totalApiCallRepository.saveAll(updatedCalls);
    }
}
