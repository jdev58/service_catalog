package com.fanhab.portal.portal.repository;


import com.fanhab.portal.portal.model.BillingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * {@link Message} repository.
 */
@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Long>, JpaSpecificationExecutor<BillingDetail> {

}
