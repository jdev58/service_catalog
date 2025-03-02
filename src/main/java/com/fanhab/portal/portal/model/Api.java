package com.fanhab.portal.portal.model;


import com.fanhab.portal.dto.enums.ApiCategoryEnum;
import com.fanhab.portal.dto.enums.ProviderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "svc_api", indexes = {
        @Index(unique = true, name = "IDX_PRT_API_CODE", columnList = "API_CODE")
})
public class Api extends BaseDomain{
    @Column(name = "API_CODE", nullable = false)
    String apiCode;

    @Column(name = "API_NAME", nullable = false)
    String apiName;

    @Column(name = "API_PERSIAN_NAME")
    String apiPersianName;

    @Column(name = "API_DESCR", nullable = false)
    String apiDescr;

    @Column(name = "API_VERSION", nullable = false)
    String apiVersion;


    @Column(name = "API_PROVIDER", nullable = false)
    @Enumerated(EnumType.STRING)
    ProviderEnum provider;

    @Column(name = "API_DESCRPTION")
    String apiDescription;


    @Column(name = "API_CATEGORY")
    @Enumerated(EnumType.STRING)
    ApiCategoryEnum category;
}
