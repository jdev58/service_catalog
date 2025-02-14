package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.wso2.dto.ApiRequestDto;
import org.springframework.stereotype.Component;

@Component
public class TotalApiCallMapper {
    public ApiRequestDto mapObjectApiRequestDto(Object[] objects){
        if (objects == null || objects.length < 4) {
            throw new IllegalArgumentException("Invalid input data for mapping to ApiRequestDto");
        }

        String applicationOwner = (String) objects[0];
        String apiName = (String) objects[1];
        Integer responseCode = (Integer) objects[2];
        Long totalCount = ((Number) objects[3]).longValue();

        return new ApiRequestDto(applicationOwner, apiName, responseCode, totalCount);

    }
}
