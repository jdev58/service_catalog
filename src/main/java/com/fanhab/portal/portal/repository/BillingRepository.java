package com.fanhab.portal.portal.repository;


import com.fanhab.portal.portal.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * {@link Message} repository.
 */
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>, JpaSpecificationExecutor<Billing> {

}
