package com.fanhab.portal.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableJpaRepositories(
        basePackages = "com.fanhab.portal.portal.repository",
        entityManagerFactoryRef = "portalEntityManagerFactory",
        transactionManagerRef = "portalTransactionManager"
)
public class PortalJpaConfig {
    @Autowired
    private DataSource portalDataSource;

    @Primary
    @Bean(name = "portalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean portalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(portalDataSource)
                .packages("com.fanhab.portal.portal.model")
                .persistenceUnit("portalPU")
                .properties(hibernateProperties())
                .build();
    }

    @Primary
    @Bean(name = "portalTransactionManager")
    public PlatformTransactionManager portalTransactionManager(EntityManagerFactory portalEntityManagerFactory) {
        return new JpaTransactionManager(portalEntityManagerFactory);
    }
    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}
