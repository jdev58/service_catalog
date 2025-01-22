package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.ApiCategoryEnum;
import com.fanhab.portal.dto.enums.ProviderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "prt_api_catalog", indexes = {
        @Index(unique = true, name = "IDX_PRT_API_CODE", columnList = "API_CODE")
})
public class ApiCatalog extends BaseDomain{
    @Column(name = "API_CODE", nullable = false)
    private String apiCode;

    @Column(name = "API_NAME", nullable = false)
    private String apiName;

    @Column(name = "API_DESCR", nullable = false)
    private String apiDescr;

    @Column(name = "API_VERSION", nullable = false)
    private String apiVersion;


    @Column(name = "API_PROVIDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;


    @Column(name = "API_CATEGORY")
    @Enumerated(EnumType.STRING)
    ApiCategoryEnum category;
}
