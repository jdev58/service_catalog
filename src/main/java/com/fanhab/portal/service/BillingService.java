package com.fanhab.portal.service;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.BillStatus;
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
    public void createBillingAndDetailsForNotCalculatedApiCalls(CreateBillingDto createBillingDto) {
        int pageIndex = 0;
        int pageSize = 100;

        while (true){
            Page<TotalApiCall> totalApiCallPage = fetchPagedTotalApiCall(createBillingDto, pageIndex, pageSize);
            if (totalApiCallPage.isEmpty()) {
                break;
            }

            processEntities(entityPage.getContent(), relatedEntities);
            pageSize++;
        }

    }

    private Page<TotalApiCall> fetchPagedTotalApiCall(CreateBillingDto createBillingDto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return totalApiCallRepository.findByContractIdAndTotalApiCallCountBetweenAndProcessStatusOrderByTotalApiCallCountAsc(
                createBillingDto.getContractId(),
                createBillingDto.getFromDate(),
                createBillingDto.getToDate(),
                ProcessStatusEnum.NOT_CALCULATED,
                pageable
        );
    }





}
