package com.fanhab.portal.controller;

import com.fanhab.portal.dto.enums.GenerateTypeEnum;
import com.fanhab.portal.dto.request.CreateBillingDto;
import com.fanhab.portal.dto.response.BillingDto;
import com.fanhab.portal.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
