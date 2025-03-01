package com.fanhab.portal.service;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.dto.enums.GenerateTypeEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.dto.request.DebitDto;
import com.fanhab.portal.dto.request.StatusBillingDto;
import com.fanhab.portal.dto.response.BillingDetailDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.dto.response.DebitResponseDto;
import com.fanhab.portal.exception.ServiceException;
import com.fanhab.portal.exception.WriteException;
import com.fanhab.portal.mapper.BillingDetailMapper;
import com.fanhab.portal.mapper.BillingMapper;
import com.fanhab.portal.portal.model.Billing;
import com.fanhab.portal.portal.model.BillingDetail;
import com.fanhab.portal.portal.model.TotalApiCall;
import com.fanhab.portal.portal.repository.*;
import com.fanhab.portal.service.proxy.ledger.LedgerDebitService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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
    private BillingDetailMapper billingDetailMapper;
    @Autowired
    private TotalApiCallService totalApiCallService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ApiCatalogRepository apiCatalogRepository;
    @Autowired
    private LedgerDebitService ledgerDebitService;

    public List<BillingDto> createBillingAndDetailsForNotCalculatedApiCalls(CreateBillingDto createBillingDto, GenerateTypeEnum generateTypeEnum) {
        createBillingDto.setFromDate(convertTimestampToLocalDate(createBillingDto.getStartDate()));
        createBillingDto.setToDate(convertTimestampToLocalDate(createBillingDto.getEndDate()));
        if (generateTypeEnum == GenerateTypeEnum.SYSTEM &&
                Stream.of(createBillingDto.getCompanyId(), createBillingDto.getContractId()).anyMatch(Objects::nonNull)) {
            throw ServiceException.badRequestException("For auto-generate, contract and company should be null.");
        }
        validateDates(createBillingDto.getFromDate(),createBillingDto.getToDate());
        checkbillingWithinTimeRange(null,createBillingDto.getFromDate(),createBillingDto.getToDate(),null);
        totalApiCallService.createTotalapiCall(createBillingDto);
        int pageIndex = 0;
        int pageSize = 1000;
        List<BillingDto> billingDtoList = new ArrayList<>();
        while (true){
            Page<TotalCallApiDto> totalApiCallPage = fetchPagedTotalApiCall(createBillingDto, pageIndex, pageSize);
            if(pageIndex == 0 && totalApiCallPage.isEmpty() ){
                throw ServiceException.notFoundException("there are no not-calculated data in totalApiCall table",HttpStatus.NOT_FOUND);
            }
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
                createBillingDto.getCompanyId(),
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

            List<BillingDetailDto> billingDetailDtos = new ArrayList<>();
            for(TotalCallApiDto totalCallApiDto:apiDtos){
                BillingDetail billingDetail = new BillingDetail();
                billingDetail.setBillingId(billing.getId());
                billingDetail.setApiId(totalCallApiDto.getApiId());
                billingDetail.setApi(apiCatalogRepository.findById(totalCallApiDto.getApiId()).get());
                billingDetail.setApiResponseCode(totalCallApiDto.getApiStatus());
                billingDetail.setApiTotalAmount(totalCallApiDto.getTotalAmount());
                billingDetail.setTotalApiCallCount(totalCallApiDto.getTotalApiCount());
                billingDetail.setPrice(totalCallApiDto.getPerPrice());



                BillingDetailDto billingDetailDto = billingDetailMapper.mapEntityToDto(
                        billingDetailRepository.save(billingDetail));
                billingDetailDtos.add(billingDetailDto);
            }
            BillingDto billingDto = billingMapper.mapEntityToDto(billing, billingDetailDtos);
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
    public BillingDto getBillingDetail(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Billing not found for ID: " + id));
        List<BillingDetailDto> billingDetailDtos = billingDetailRepository.findByBillingId(billing.getId()).stream().map(billingDetailMapper::mapEntityToDto).collect(Collectors.toList());
        return billingMapper.mapEntityToDto(billing, billingDetailDtos);
    }

    public Page<BillingDto> getAllBillingByCompanyId(Long companyId, Long startDate, Long endDate, BillStatusEnum billStatus, Integer pageIndex, Integer pageSize) {
        var fromDate = startDate != null ? convertTimestampToLocalDate(startDate) : null;
        var toDate = endDate != null ? convertTimestampToLocalDate(endDate) : null;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Billing> billingList = billingRepository.findBillingByCompanyId(companyId,
                fromDate,
                toDate,
                billStatus , pageable);

        List<BillingDto> billingDtos = billingList.getContent()
                .stream()
                .map(billingMapper::mapBillingEntityToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(billingDtos, pageable, billingList.getTotalElements());
    }

    private void checkbillingWithinTimeRange(Long contractId,LocalDate startDate, LocalDate endDate,BillStatusEnum billStatusEnum) {
        List<Billing> billings = billingRepository.findBillingWithinTimeRange(contractId,startDate,endDate,billStatusEnum);
        if (!billings.isEmpty()) {
            StringBuilder message = new StringBuilder("صورت وضعیت حساب ها با شماره قرارداد زیر در بازه زمانی موردنظر ایجاد و تایید شده‌اند: ");

            billings.forEach(billing ->
                    message.append("\nContract ID: ").append(billing.getContract().getContractNumber())
                            .append(", Start Date: ").append(billing.getFromDate())
                            .append(", End Date: ").append(billing.getToDate())
            );

            throw WriteException.alreadyExistException("billing", message.toString(), HttpStatus.CONFLICT);
        }
    }



    public DebitResponseDto paidBlling(StatusBillingDto statusBillingDto){
        Billing billing = billingRepository.findByIdAndBillStatus(statusBillingDto.getBillingId(),BillStatusEnum.VERIFIED);
        if(billing == null)
            throw ServiceException.notFoundException("Veified Billing not found for ID: " + statusBillingDto.getBillingId(),HttpStatus.NOT_FOUND);
        DebitDto debitDto = billingMapper.mapEntityToDebitDto(billing);
        DebitResponseDto responseDto = ledgerDebitService.sendToDebit(debitDto);
        if (responseDto != null){
            updateBillingStatus(billing.getId(),BillStatusEnum.PAID);
        }
        return responseDto;
    }
    public BillingDto verifiedBilling(StatusBillingDto statusBillingDto){
        Long id = statusBillingDto.getBillingId();
        Billing billing = billingRepository.findByIdAndBillStatus(id,BillStatusEnum.READY);
        if(billing == null)
            throw ServiceException.notFoundException("Ready Billing not found for ID: " + statusBillingDto.getBillingId(),HttpStatus.NOT_FOUND);
        return billingMapper.mapBillingEntityToDto(updateBillingStatus(billing.getId(),BillStatusEnum.VERIFIED));
    }
    @Transactional
    public BillingDto rejectedBilling(StatusBillingDto statusBillingDto){
        Long id = statusBillingDto.getBillingId();
        Billing billing = billingRepository.findByIdAndBillStatus(id,BillStatusEnum.READY);
        if(billing == null)
            throw ServiceException.notFoundException("Ready Billing not found for ID: " + statusBillingDto.getBillingId(),HttpStatus.NOT_FOUND);
        List<TotalApiCall> totalApiCall = totalApiCallRepository.findCalculateTotalApiCall(
                billing.getContractId(),
                billing.getFromDate().atStartOfDay(),
                billing.getToDate().atTime(23, 59, 59));
        totalApiCall.forEach(t -> changeTotalApiCallStatus(t, ProcessStatusEnum.NOT_CALCULATED));
        return billingMapper.mapBillingEntityToDto(updateBillingStatus(billing.getId(),BillStatusEnum.REJECTED));
    }

    private TotalApiCall changeTotalApiCallStatus(TotalApiCall totalApiCall,ProcessStatusEnum processStatusEnum){
        totalApiCall.setProcessState(processStatusEnum);
        return totalApiCallRepository.save(totalApiCall);
    }
    @Transactional
    public Billing updateBillingStatus(Long id, BillStatusEnum newStatus) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Billing not found for ID: " + id));
        billing.setBillStatus(newStatus);
        Billing savedBilling = billingRepository.save(billing);
        return savedBilling;
    }
    public void softDeleteBilling(Long id) {
        billingRepository.softDeleteById(id);
        billingDetailRepository.softDeleteByBillingId(id);
    }
}
