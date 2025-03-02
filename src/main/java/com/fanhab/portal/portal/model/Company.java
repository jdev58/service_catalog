package com.fanhab.portal.portal.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)

@Table(name = "svc_company")
public class Company extends BaseDomain {
    @Column(name = "COMPANY_NAME")
    String companyName;
    @Column(name = "COMPANY_PERSIAN_NAME")
    String companyPersianName;
    @Column(name = "COMPANY_MOBILE")
    String companyMobile;

    @Column(name = "COMPANY_PHONE")
    String companyPhone;

    @Column(name = "COMPANY_EMAIL")
    String companyEmail;

    @Column(name = "COMPANY_CEO")
    String companyCeo;

    @Column(name = "persian_brand_name")
    String persianBrandName;

    @Column(name = "english_brand_name")
    String englishBrandName;



    @Column(name = "server_ip")
    String serverIp;

    @Column(name = "verification_code")
    String verificationCode;

    @Column(name = "reason_description")
    String reasonDescription;
}
