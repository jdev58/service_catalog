package com.fanhab.portal.service;

import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.dto.response.ContractInfoDto;
import com.fanhab.portal.mapper.ContractInfoMapper;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.repository.ContractApiRepository;
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

    public List<ContractInfoDto> getContractInfo(){
        List<Contract> contracts = contractRepository.findByContractStatus(StatusEnum.ACTIVE);

        List<ContractInfoDto> contractInfoDtoList = contracts.stream().map(contract -> {
            List<ApiDto> apiDtoList = contractApiRepository.findByContractId(contract.getId()).stream()
                    .map(contractAPI -> contractInfoMapper.apiEntityToDto(contractAPI.getApiCatalog()
                    ))
                    .collect(Collectors.toList());

            return contractInfoMapper.contractEntityToDto(contract,apiDtoList);
        }).collect(Collectors.toList());
        return contractInfoDtoList;
    }
}
