package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.dto.response.ContractInfoDto;
import com.fanhab.portal.portal.model.Api;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.model.ContractAPI;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractInfoMapper {
    public ContractInfoDto contractEntityToDto(Contract contract, List<ApiDto> apiDto){
        ContractInfoDto contractInfoDto = new ContractInfoDto(
                contract.getId(),
                contract.getContractNumber(),
                contract.getCompany().getCompanyName(),
                apiDto
                );
        return contractInfoDto;
    }
    public ApiDto apiEntityToDto(Api api){
        ApiDto apiDto = new ApiDto(
                api.getApiCode(),
                api.getApiName()
        );
        return apiDto;
    }
}
