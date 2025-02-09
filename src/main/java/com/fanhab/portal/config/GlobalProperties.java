package com.fanhab.portal.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@PropertySource({"classpath:global.properties"})
@PropertySource(value = "classpath:global.properties", encoding = "UTF-8")
@Data
@ToString
public class GlobalProperties {



    private Boolean isActiveTrustSSL;
    private String pageSize="10";
    private String pageIndex="1";
    private String ledger_debit_account_url;


    public int getIntPageSize(){
        return Integer.parseInt(pageSize);
    }

    public int getIntPageIndex(){
        return Integer.parseInt(pageIndex);
    }

    public Boolean getIsActiveTrustSSL() {
        return Boolean.valueOf(isActiveTrustSSL);
    }
}

