package com.fanhab.portal.wso2.repository;

import com.fanhab.portal.wso2.dto.ApiRequestDto;
import com.fanhab.portal.wso2.model.ApiRequestsTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiRequestsTestRepository extends JpaRepository<ApiRequestsTest,Long> {
//    @Query("SELECT new com.fanhab.portal.wso2.dto.ApiRequestDto(" +
//            " a.applicationOwner, a.apiName, a.responseCode, COUNT(a.responseCode) )" +
//            "FROM ApiRequestsTest a " +
//            "WHERE a.responseCode IN (200, 231) " +
//            "AND a.requestTimestamp BETWEEN :startDate AND :endDate " +
//            "GROUP BY a.applicationOwner, a.apiName, a.responseCode " +
//            "ORDER BY a.applicationOwner ASC")
//    List<ApiRequestDto> findApiRequestsGrouped(@Param("startDate") Long startDate,
//                                               @Param("endDate") Long endDate);

    @Query(value = "SELECT a.applicationowner, a.apiname, a.responsecode, COUNT(a.responsecode) as total_count " +
            "FROM api_Requeststest a " +
            "WHERE a.responsecode IN (200, 231) " +
            "AND a.requesTtimestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY a.applicationowner, a.apiname, a.responsecode " +
            "ORDER BY a.applicationowner ASC", nativeQuery = true)
    List<Object[]> findApiRequestsGrouped(@Param("startDate") Long startDate, @Param("endDate") Long endDate);



}