package com.fanhab.portal.portal.model;


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

@Table(name = "svc_company")
public class Company extends BaseDomain {
    @Column(name = "COMPANY_NAME")
    String companyName;
    @Column(name = "COMPANY_PERSIAN_NAME")
    String companyPersianName;
}
