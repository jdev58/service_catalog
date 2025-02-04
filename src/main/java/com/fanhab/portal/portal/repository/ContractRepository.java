package com.fanhab.portal.portal.repository;


import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.portal.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    List<Contract> findByContractStatus(StatusEnum statusEnum);
}
