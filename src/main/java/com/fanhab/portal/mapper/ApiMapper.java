package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.ApiDto;
import com.fanhab.portal.portal.model.Api;
import org.springframework.stereotype.Component;

@Component
public class ApiMapper {
    public Api mapDtoToEntity(ApiDto apiDto) {
        Api api = new Api();
        return api;
    }
    public ApiDto mapEntityToDto(Api api) {
        ApiDto apiDto = new ApiDto();
        return apiDto;
    }
}
