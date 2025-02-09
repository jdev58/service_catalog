package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.portal.model.Billing;
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
public interface BillingRepository extends JpaRepository<Billing, Long>, JpaSpecificationExecutor<Billing> {
    @Query("SELECT t FROM Billing t WHERE (t.fromDate <= :startDate and  t.toDate >= :startDate)" +
            "or (t.fromDate <= :endDate and  t.toDate >= :endDate) and t.isDeleted <> true ")
    List<Billing> findbillingWithinTimeRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Billing> findByBillStatus(BillStatusEnum billStatusEnum);

}
