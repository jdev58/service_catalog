package com.fanhab.portal.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.fanhab.portal.wso2.repository",
        entityManagerFactoryRef = "wso2EntityManagerFactory",
        transactionManagerRef = "wso2TransactionManager"
)
public class Wso2JpaConfig {

    @Autowired
    @Qualifier("wso2DataSource")
    private DataSource wso2DataSource;

    @Bean(name = "wso2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean wso2EntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return builder
                .dataSource(wso2DataSource)
                .packages("com.fanhab.portal.wso2.model")
                .persistenceUnit("wso2PU")
                .properties(properties)
                .build();
    }

    @Bean(name = "wso2TransactionManager")
    public PlatformTransactionManager wso2TransactionManager(EntityManagerFactory wso2EntityManagerFactory) {
        return new JpaTransactionManager(wso2EntityManagerFactory);
    }
}
