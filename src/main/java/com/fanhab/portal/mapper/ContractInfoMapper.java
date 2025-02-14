package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.dto.response.ContractInfoDto;
import com.fanhab.portal.dto.response.PricePerStatus;
import com.fanhab.portal.portal.model.Api;
import com.fanhab.portal.portal.model.Contract;
import com.fanhab.portal.portal.model.ContractAPI;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fanhab.portal.utils.DateUtils.convertLocalDateTimeToTimestamp;
import static com.fanhab.portal.utils.DateUtils.convertTimestampToLong;

@Component
public class ContractInfoMapper {
    public ContractInfoDto contractEntityToDto(Contract contract, List<ApiDto> apiDto){
        ContractInfoDto contractInfoDto = new ContractInfoDto(
                contract.getId(),
                contract.getContractNumber(),
                contract.getCompany().getCompanyName(),
                convertTimestampToLong(convertLocalDateTimeToTimestamp(contract.getStartDate())),
                convertTimestampToLong(convertLocalDateTimeToTimestamp(contract.getEndDate())),
                apiDto
                );
        return contractInfoDto;
    }
    public ApiDto apiEntityToDto(Api api,List<PricePerStatus> pricePerStatuses){
        ApiDto apiDto = new ApiDto(
                api.getApiCode(),
                api.getApiName(),
                pricePerStatuses
        );
        return apiDto;
    }
    public PricePerStatus priceMapToPricePerStatusDto(ApiStatusEnum apiStatusEnum,Integer perPrice){
        PricePerStatus pricePerStatus = new PricePerStatus(apiStatusEnum,perPrice);
        return pricePerStatus;
    }
}
