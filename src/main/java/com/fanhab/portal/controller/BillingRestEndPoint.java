package com.fanhab.portal.controller;

import com.fanhab.portal.dto.enums.GenerateTypeEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.dto.request.VerfiyBillingDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.dto.response.DebitResponseDto;
import com.fanhab.portal.service.BillingService;
import com.fanhab.portal.service.proxy.ledger.LedgerDebitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/ShowBilling")
    public ResponseEntity<List<BillingDto>> showBilling(@RequestBody CreateBillingDto createBillingDto){
        List<BillingDto> billingDtoList = billingService.showBillingAndDetail(createBillingDto);
        return new ResponseEntity<>(billingDtoList,HttpStatus.OK);
    }


    @PostMapping("/VerifyBilling")
    public ResponseEntity<BillingDto> verifyBilling(@RequestBody VerfiyBillingDto verfiyBillingDto){
        BillingDto billingDto = billingService.verifiedBilling(verfiyBillingDto);
        return new ResponseEntity<>(billingDto,HttpStatus.OK);
    }
    @PostMapping("/PayBilling")
    public ResponseEntity<DebitResponseDto> payBilling(@RequestBody VerfiyBillingDto verfiyBillingDto){
        DebitResponseDto debitResponseDtos = billingService.paidBlling(verfiyBillingDto);
        return new ResponseEntity<>(debitResponseDtos,HttpStatus.OK);
    }
    @GetMapping("/GetAllCompanyBilling")
    public ResponseEntity<List<BillingDto>> getAllBilling(@RequestParam(name = "companyId", required = true) Long companyId){
        List<BillingDto> billingDtoList = billingService.getAllBillingByCompanyId(companyId);
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
