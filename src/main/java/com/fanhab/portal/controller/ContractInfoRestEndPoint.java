package com.fanhab.portal.controller;

import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.dto.response.CompanyDto;
import com.fanhab.portal.dto.response.ContractInfoDto;
import com.fanhab.portal.service.ContractInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Contract")
public class ContractInfoRestEndPoint {
    @Autowired
    private ContractInfoService contractInfoService;
    @GetMapping("/ContractInfo")
    public ResponseEntity<List<ContractInfoDto>> findByProcese(){
        List<ContractInfoDto> contractInfoDtoList = contractInfoService.getContractInfo();
        return new ResponseEntity<>(contractInfoDtoList, HttpStatus.OK);
    }
    @GetMapping("/CompanyList")
    public ResponseEntity<List<CompanyDto>> getCompanyList(){
        List<CompanyDto> companyDtoList = contractInfoService.getAllCompanyInfo();
        return new ResponseEntity<>(companyDtoList, HttpStatus.OK);
    }

//    @GetMapping("/ServiceList")
//    public ResponseEntity<List<ApiDto>> getServiceList(){
//        List<ApiDto> apiDtos = contractInfoService.getAllCompanyInfo();
//        return new ResponseEntity<>(apiDtos, HttpStatus.OK);
//    }
}
