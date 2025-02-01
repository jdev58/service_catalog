package com.fanhab.portal.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties"})
public class Wso2DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSourceProperties wso2DataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "wso2DataSource")
    public DataSource wso2DataSource() {
        return wso2DataSourceProperties().initializeDataSourceBuilder().build();
    }
}
