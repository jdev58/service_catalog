package com.fanhab.portal.service;

import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.dto.response.ContractInfoDto;
import com.fanhab.portal.dto.response.PricePerStatus;
import com.fanhab.portal.mapper.ContractInfoMapper;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.repository.ContractApiRepository;
import com.fanhab.portal.portal.repository.ContractDetailApiRepository;
import com.fanhab.portal.portal.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractInfoService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractInfoMapper contractInfoMapper;
    @Autowired
    private ContractApiRepository contractApiRepository;
    @Autowired
    private ContractDetailApiRepository contractDetailApiRepository;

    public List<ContractInfoDto> getContractInfo(){
        List<Contract> contracts = contractRepository.findByContractStatus(StatusEnum.ACTIVE);

        List<ContractInfoDto> contractInfoDtoList = contracts.stream().map(contract -> {

            List<ApiDto> apiDtoList = contractApiRepository.findByContractId(contract.getId()).stream()
                    .map(contractAPI ->{
                        List<PricePerStatus> pricePerStatuses = contractDetailApiRepository.findByContractIdAndApiId(contract.getId(),contractAPI.getApiId()).stream()
                                .map(contractDetailAPI -> contractInfoMapper.priceMapToPricePerStatusDto(
                                        contractDetailAPI.getApiStatus(),contractDetailAPI.getPrice()
                                ))
                                .collect(Collectors.toList());
                        return contractInfoMapper.apiEntityToDto(contractAPI.getApiCatalog(),pricePerStatuses);
                    })
                    .collect(Collectors.toList());

            return contractInfoMapper.contractEntityToDto(contract,apiDtoList);
        }).collect(Collectors.toList());
        return contractInfoDtoList;
    }
}
