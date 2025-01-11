package com.fanhab.portal.repository;


import com.fanhab.portal.model.Billing;
import com.fanhab.portal.model.TotalApiCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * {@link Message} repository.
 */
@Repository
public interface TotalApiCallRepository extends JpaRepository<TotalApiCall, Long>, JpaSpecificationExecutor<TotalApiCall> {

}
