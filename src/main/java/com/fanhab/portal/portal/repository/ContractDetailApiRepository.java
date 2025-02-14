package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.portal.model.ContractAPI;
import com.fanhab.portal.portal.model.ContractDetailAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface ContractDetailApiRepository extends JpaRepository<ContractDetailAPI, Long>, JpaSpecificationExecutor<ContractDetailAPI> {
    List<ContractDetailAPI> findByContractIdAndApiIdAndApiStatus(Long contractId, Long apiId, ApiStatusEnum apiStatus);
    List<ContractDetailAPI> findByContractIdAndApiId(Long contractId, Long apiId);
    @Query("SELECT cda FROM ContractDetailAPI cda " +
            "JOIN cda.contract c " +
            "WHERE c.contractStatus = :status")
    List<ContractDetailAPI> findByActiveContract(@Param("status") StatusEnum status);

}
