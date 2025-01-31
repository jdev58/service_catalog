package com.fanhab.portal.repository;

import com.fanhab.portal.dto.ApiRequestDto;
import com.fanhab.portal.dto.TotalCallApiDto;
import com.fanhab.portal.model.ApiRequestsTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiRequestsTestRepository extends JpaRepository<ApiRequestsTest,Long> {
    @Query("SELECT new com.fanhab.portal.dto.ApiRequestDto(" +
            " a.applicationOwner, a.apiName, a.responseCode, COUNT(a.responseCode) )" +
            "FROM ApiRequestsTest a " +
            "WHERE a.responseCode IN (200, 231) " +
            "AND a.requestTimestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY a.applicationOwner, a.apiName, a.responseCode " +
            "ORDER BY a.applicationOwner ASC")
    List<ApiRequestDto> findApiRequestsGrouped(@Param("startDate") Long startDate,
                                               @Param("endDate") Long endDate);

}