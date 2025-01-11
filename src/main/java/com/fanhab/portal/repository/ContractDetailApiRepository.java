package com.fanhab.portal.repository;


import com.fanhab.portal.model.Billing;
import com.fanhab.portal.model.ContractDetailAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * {@link Message} repository.
 */
@Repository
public interface ContractDetailApiRepository extends JpaRepository<ContractDetailAPI, Long>, JpaSpecificationExecutor<ContractDetailAPI> {

}
