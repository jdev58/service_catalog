package com.fanhab.portal.repository;


import com.fanhab.portal.model.ApiCatalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Message} repository.
 */
@Repository
public interface ApiCatalogRepository extends JpaRepository<ApiCatalog, Long>, JpaSpecificationExecutor<ApiCatalog> {

}
