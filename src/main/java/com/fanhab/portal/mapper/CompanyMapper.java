package com.fanhab.portal.mapper;

import com.fanhab.portal.dto.response.CompanyDto;
import com.fanhab.portal.portal.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyDto mapEntityToDto(Company company) {
        CompanyDto companyDto = new CompanyDto(
                company.getId(),
                company.getCompanyName(),
                company.getCompanyPersianName()
        );
        return companyDto;
    }
}
