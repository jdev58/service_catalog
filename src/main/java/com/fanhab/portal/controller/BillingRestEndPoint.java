package com.fanhab.portal.controller;

import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.dto.enums.GenerateTypeEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.dto.request.StatusBillingDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.dto.response.DebitResponseDto;
import com.fanhab.portal.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/Billing")
public class BillingRestEndPoint {
    @Autowired
    private BillingService billingService;

//    @GetMapping("/apiCall")
//    public ResponseEntity<List<TotalApiCall>> findByProcese(@RequestParam ProcessStatusEnum process){
//        List<TotalApiCall> aa = repository.findByProcessState(process);
//        return new ResponseEntity<>(aa, HttpStatus.OK);
//    }
    @PostMapping("/AutoGenerate")
    public ResponseEntity<List<BillingDto>> autoSave(@RequestBody CreateBillingDto createBillingDto){
        List<BillingDto> billingDtoList = billingService.createBillingAndDetailsForNotCalculatedApiCalls(createBillingDto, GenerateTypeEnum.SYSTEM);
        return new ResponseEntity<>(billingDtoList,HttpStatus.CREATED);
    }
    @PostMapping("/ManualGenerate")
    public ResponseEntity<List<BillingDto>> manualSave(@RequestBody CreateBillingDto createBillingDto){
        List<BillingDto> billingDtoList = billingService.createBillingAndDetailsForNotCalculatedApiCalls(createBillingDto,GenerateTypeEnum.MANUAL);
        return new ResponseEntity<>(billingDtoList,HttpStatus.CREATED);
    }

    @PostMapping("/VerifyBilling")
    public ResponseEntity<BillingDto> verifyBilling(@RequestBody StatusBillingDto statusBillingDto){
        BillingDto billingDto = billingService.verifiedBilling(statusBillingDto);
        return new ResponseEntity<>(billingDto,HttpStatus.OK);
    }
    @PostMapping("/RejectBilling")
    public ResponseEntity<BillingDto> rejectBilling(@RequestBody StatusBillingDto statusBillingDto){
        BillingDto billingDto = billingService.rejectedBilling(statusBillingDto);
        return new ResponseEntity<>(billingDto,HttpStatus.OK);
    }
    @PostMapping("/PayBilling")
    public ResponseEntity<DebitResponseDto> payBilling(@RequestBody StatusBillingDto statusBillingDto){
        DebitResponseDto debitResponseDtos = billingService.paidBlling(statusBillingDto);
        return new ResponseEntity<>(debitResponseDtos,HttpStatus.OK);
    }
    @GetMapping("/BillingStatus")
    public ResponseEntity<List<String>> getSearchTypes() {
        return new ResponseEntity<>(Stream.of(BillStatusEnum.values()).map(Enum::name).collect(Collectors.toList()), HttpStatus.OK);
    }
    @GetMapping("/GetAllCompanyBilling")
    public ResponseEntity<Page<BillingDto>> getAllBilling(@RequestParam(name = "companyId", required = false) Long companyId,
                                                          @RequestParam(name = "startDate", required = false) Long startDate,
                                                          @RequestParam(name = "endDate", required = false) Long endDate,
                                                          @RequestParam(name = "billingStatus", required = false) BillStatusEnum billStatus,
                                                          @RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Page<BillingDto> billingDtoList = billingService.getAllBillingByCompanyId(companyId,startDate,endDate,billStatus,pageIndex,pageSize);
        return new ResponseEntity<>(billingDtoList,HttpStatus.OK);
    }
    @GetMapping("/GetBillingDetail")
    public ResponseEntity<BillingDto> getBillingDetail(@RequestParam(name = "billingId", required = true) Long billingId){
        BillingDto billingDtoList = billingService.getBillingDetail(billingId);
        return new ResponseEntity<>(billingDtoList,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteBilling(@PathVariable Long id) {
        billingService.softDeleteBilling(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
