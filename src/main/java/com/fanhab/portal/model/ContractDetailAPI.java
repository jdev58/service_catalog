package com.fanhab.portal.model;


import com.fanhab.portal.dto.enums.ApiStatusEnum;
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
@Table(name = "prt_contract_detail_api")
public class ContractDetailAPI extends BaseDomain {
    @Column(name = "CONTRACT_ID")
    Long contractId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACTDETAIL_CONTRACT"))
    Contract contract;



    @Column(name = "API_ID")
    Long apiId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "API_ID", referencedColumnName = "ID",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CONTRACTDETAIL_API"))
    ApiCatalog apiCatalog;


    @Column(name = "min_call_no")
    Integer minCallNo;

    @Column(name = "max_call_no")
    Integer maxCallNo;

    @Column(name = "per_call_price")
    Integer price;


    @Column(name = "apiStatus")
    @Enumerated(EnumType.STRING)
    ApiStatusEnum apiStatus;


}
