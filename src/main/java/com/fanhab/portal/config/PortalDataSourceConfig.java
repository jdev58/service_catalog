package com.fanhab.portal.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class PortalDataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSourceProperties portalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource portalDataSource() {
        return portalDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
