package com.fanhab.portal.service;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.dto.enums.GenerateTypeEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.exception.ServiceException;
import com.fanhab.portal.exception.WriteException;
import com.fanhab.portal.mapper.BillingMapper;
import com.fanhab.portal.portal.model.Billing;
import com.fanhab.portal.portal.model.BillingDetail;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.model.TotalApiCall;
import com.fanhab.portal.portal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fanhab.portal.utils.DateUtils.convertTimestampToLocalDate;

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
    @Autowired
    private TotalApiCallService totalApiCallService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ApiCatalogRepository apiCatalogRepository;

    public List<BillingDto> createBillingAndDetailsForNotCalculatedApiCalls(CreateBillingDto createBillingDto, GenerateTypeEnum generateTypeEnum) {
        createBillingDto.setFromDate(convertTimestampToLocalDate(createBillingDto.getStartDate()));
        createBillingDto.setToDate(convertTimestampToLocalDate(createBillingDto.getEndDate()));
        if (generateTypeEnum == GenerateTypeEnum.SYSTEM &&
                Stream.of(createBillingDto.getCompanyId(), createBillingDto.getContractId()).anyMatch(Objects::nonNull)) {
            throw ServiceException.badRequestException("For auto-generate, contract and company should be null.");
        }
        validateDates(createBillingDto.getFromDate(),createBillingDto.getToDate());
        checkbillingWithinTimeRange(createBillingDto.getFromDate(),createBillingDto.getToDate());
        totalApiCallService.createTotalapiCall(createBillingDto);
        int pageIndex = 0;
        int pageSize = 1000;
        List<BillingDto> billingDtoList = new ArrayList<>();
        while (true){
            Page<TotalCallApiDto> totalApiCallPage = fetchPagedTotalApiCall(createBillingDto, pageIndex, pageSize);
            if (totalApiCallPage.isEmpty()) {
                break;
            }
            Map<Long, List<TotalCallApiDto>> apiCallGroupedByContractId = groupedByContractId(totalApiCallPage.getContent());
            billingDtoList = createBillingAndBillingDetail(apiCallGroupedByContractId,createBillingDto);
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
                createBillingDto.getToDate().atTime(23, 59, 59),
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
    private List<BillingDto> createBillingAndBillingDetail(Map<Long, List<TotalCallApiDto>> groupedApiCall,CreateBillingDto createBillingDto){
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
            billing.setCompany(companyRepository.findById(companyId).get());
            billing.setContractId(contractId);
            billing.setContract(contractRepository.findById(contractId).get());
            billing.setTotalAmount(totalAmount);
            billing.setFromDate(createBillingDto.getFromDate());
            billing.setToDate(createBillingDto.getToDate());
            billing.setBillStatus(BillStatusEnum.READY);

            billing = billingRepository.save(billing);

            List<BillingDetail> billingDetails = new ArrayList<>();
            for(TotalCallApiDto totalCallApiDto:apiDtos){
                BillingDetail billingDetail = new BillingDetail();
                billingDetail.setBillingId(billing.getId());
                billingDetail.setApiId(totalCallApiDto.getApiId());
                billingDetail.setApi(apiCatalogRepository.findById(totalCallApiDto.getApiId()).get());
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
    public void validateDates(LocalDate startDate,LocalDate endDate) {


        if(startDate == null || endDate == null){
            throw ServiceException.badRequestException("تاریخ شروع و پایان نمیتواند خالی باشد");
        }



        if (startDate.isAfter(endDate)) {
            throw ServiceException.badRequestException("تاریخ شروع نمی‌تواند بزرگتر از تاریخ پایان باشد.");
        }


        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now())  || endDate.isEqual(LocalDate.now())) {
            throw  ServiceException.badRequestException("تاریخ شروع و پایان نمیتوانند بزرگتر یا برابر تاریخ امروز باشند.");
        }



        int daysBetween = (int) (endDate.toEpochDay() - startDate.toEpochDay());

        if (daysBetween > 31) {
            throw ServiceException.badRequestException("فاصله زمانی بین تاریخ‌ها نمی‌تواند بیش از 31 روز باشد.");
        }


    }
    private void checkbillingWithinTimeRange(LocalDate startDate, LocalDate endDate) {
        List<Billing> billings = billingRepository.findbillingWithinTimeRange(startDate,endDate);
        if (!billings.isEmpty()) {
            StringBuilder message = new StringBuilder("قبض‌ها با شماره قراراد‌های زیر در بازه زمانی موردنظر ایجاد شده‌اند: ");

            billings.forEach(billing ->
                    message.append("\nContract ID: ").append(billing.getContract().getContractNumber())
                            .append(", Start Date: ").append(billing.getFromDate())
                            .append(", End Date: ").append(billing.getToDate())
            );

            throw WriteException.alreadyExistException("billing", message.toString(), HttpStatus.CONFLICT);
        }
    }
}
