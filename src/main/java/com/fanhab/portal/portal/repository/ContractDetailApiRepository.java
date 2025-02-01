package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.portal.model.ContractDetailAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface ContractDetailApiRepository extends JpaRepository<ContractDetailAPI, Long>, JpaSpecificationExecutor<ContractDetailAPI> {
    List<ContractDetailAPI> findByContractIdAndApiIdAndApiStatus(Long contractId, Long apiId, ApiStatusEnum apiStatus);
}
