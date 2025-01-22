package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.StatusEnum;
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
@Table(name = "prt_contract")
public class Contract extends BaseDomain {
    @Column(name = "COMPANY_ID")
    Long companyId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACT_COMPANY"))
    Company company;



    @Column(name = "CONTRACT_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum contractStatus;

}
