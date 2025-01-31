package com.fanhab.portal.repository;

import com.fanhab.portal.dto.enums.StatusEnum;
import com.fanhab.portal.model.ContractAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractApiRepository extends JpaRepository<ContractAPI,Long> {
    List<ContractAPI> findByStatusEnum(StatusEnum statusEnum);
}
