package com.fanhab.portal.portal.repository;


import com.fanhab.portal.portal.model.BillingDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Long>, JpaSpecificationExecutor<BillingDetail> {

    List<BillingDetail> findByBillingId(Long billingId);

    @Modifying
    @Transactional
    @Query("UPDATE BillingDetail bd SET bd.isDeleted = true WHERE bd.billingId = :billingId")
    void softDeleteByBillingId(@Param("billingId") Long billingId);
}
