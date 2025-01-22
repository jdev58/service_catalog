package com.fanhab.portal.controller;

import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.model.TotalApiCall;
import com.fanhab.portal.repository.TotalApiCallRepository;
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
    @PostMapping
    public ResponseEntity save(){
        billingService.createBillingAndDetailsForNotCalculatedApiCalls();
        return ResponseEntity.ok("ok");
    }
}
