package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.enums.BillStatusEnum;
import com.fanhab.portal.portal.model.Billing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query("SELECT t FROM Billing t WHERE t.contractId = :contractId and ((t.fromDate <= :startDate and  t.toDate >= :startDate)" +
            "or (t.fromDate <= :endDate and  t.toDate >= :endDate) or (t.fromDate >= : startDate and t.toDate <= :endDate)) and t.isDeleted <> true and t.billStatus = :billStatus")
    List<Billing> findbillingWithinTimeRange(@Param("contractId") Long contractId,
                                             @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                             @Param("billStatus") BillStatusEnum billStatusEnum);
    @Query("SELECT t FROM Billing t WHERE " +
            "(:contractId IS NULL OR t.contractId = :contractId) AND " +
            "((t.fromDate <= :startDate AND t.toDate >= :startDate) OR " +
            "(t.fromDate <= :endDate AND t.toDate >= :endDate) OR " +
            "(t.fromDate >= :startDate AND t.toDate <= :endDate)) AND " +
            "t.isDeleted <> true AND " +
            "(:billStatus IS NULL OR t.billStatus = :billStatus)")
    List<Billing> findBillingWithinTimeRange(
            @Param("contractId") Long contractId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("billStatus") BillStatusEnum billStatus
    );

    Billing findByIdAndBillStatus(Long id,BillStatusEnum billStatusEnum);
    @Modifying
    @Transactional
    @Query("UPDATE Billing b SET b.isDeleted = true WHERE b.id = :id")
    void softDeleteById(@Param("id") Long id);

}
