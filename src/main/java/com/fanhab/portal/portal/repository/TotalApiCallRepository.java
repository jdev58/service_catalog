package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.dto.enums.ApiStatusEnum;
import com.fanhab.portal.dto.enums.ProcessStatusEnum;
import com.fanhab.portal.portal.model.TotalApiCall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface TotalApiCallRepository extends JpaRepository<TotalApiCall, Long>, JpaSpecificationExecutor<TotalApiCall> {
    @Query("SELECT new com.fanhab.portal.dto.TotalCallApiDto( " +
            " tac.id, tac.apiId, tac.contractId, tac.apiStatus, tac.totalApiCallCount,cda.price ,  cast(CAST(tac.totalApiCallCount AS long ) * cda.price as DOUBLE ), c.companyId ) " +
            "FROM TotalApiCall tac " +
            "INNER join ContractDetailAPI cda on cda.contractId = tac.contractId and cda.apiId = tac.apiId and cda.apiStatus = tac.apiStatus " +
            "inner join Contract  c on c.id = cda.contractId "+
            "WHERE tac.processState = 'NOT_CALCULATED' " +
            "AND (:companyId IS NULL OR c.companyId = :companyId) " +
            "AND tac.fromDate >= :startDate and tac.toDate <= :endDate " +
            "ORDER BY tac.contractId ASC")
    Page<TotalCallApiDto> findNotCalculatedTotalApiCalls(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT t FROM TotalApiCall t WHERE t.fromDate >= :start AND t.toDate <= :end AND t.isDeleted <> true ")
    List<TotalApiCall> findExistingTotalApiCall(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT MAX(t.toDate) FROM TotalApiCall t WHERE t.isDeleted != true ")
    LocalDateTime findMaxToDate();


    @Query("SELECT " +
            "tac.id , tac.apiId, tac.apiStatus, tac.totalApiCallCount, tac.contractId, pc.companyId " +
            "FROM TotalApiCall tac " +
            "JOIN ContractAPI ca ON ca.apiId = tac.apiId " +
            "JOIN Contract pc ON pc.id = ca.contractId " +
            "WHERE tac.processState = 'NOT_CALCULATED' AND pc.contractStatus = 'ACTIVE'")
    List<Object> findNotCalculatedTotalApiCalls2();

    List<TotalApiCall> findByApiIdAndContractIdAndApiStatusAndProcessState(Long apiId, Long contractId, ApiStatusEnum apiStatus,ProcessStatusEnum processStatus);

    Page<TotalApiCall> findByContractIdAndTotalApiCallCountBetweenAndProcessStateOrderByTotalApiCallCountAsc(Long contractId, LocalDate startDate, LocalDate endDate, ProcessStatusEnum processStatus, Pageable pageable);
}
