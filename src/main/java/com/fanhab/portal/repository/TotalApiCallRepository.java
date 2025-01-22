package com.fanhab.portal.repository;


import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.model.Billing;
import com.fanhab.portal.model.TotalApiCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface TotalApiCallRepository extends JpaRepository<TotalApiCall, Long>, JpaSpecificationExecutor<TotalApiCall> {
    @Query("SELECT new com.fanhab.portal.dto.TotalCallApiDto(" +
            "tac.id , tac.apiId, tac.apiStatus, tac.totalApiCallCount, tac.contractId, pc.companyId) " +
            "FROM TotalApiCall tac " +
            "JOIN ContractAPI ca ON ca.apiId = tac.apiId " +
            "JOIN Contract pc ON pc.id = ca.contractId " +
            "WHERE tac.processState = 'NOT_CALCULATED' AND pc.contractStatus = 'ACTIVE'")
    List<TotalCallApiDto> findNotCalculatedTotalApiCalls();

    List<TotalApiCall> findByApiIdAndContractIdAndApiStatusAndProcessState(Long apiId, Long contractId, ApiStatusEnum apiStatus,ProcessStatusEnum processStatus);
}
