package com.fanhab.portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;



@Entity
@Table(name = "apiRequeststest")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestsTest {
    @Column(name = "requestTimestamp") @Id
    private Long requestTimestamp;
    @Column(name = "meta_clientType") private String meta_clientType;
    @Column(name = "applicationConsumerKey") private String applicationConsumerKey;
    @Column(name = "applicationName") private String applicationName;
    @Column(name = "applicationId") private String applicationId;
    @Column(name = "applicationOwner") private String applicationOwner;
    @Column(name = "apiContext") private String apiContext;
    @Column(name = "apiName") private String apiName;
    @Column(name = "apiVersion") private String apiVersion;
    @Column(name = "apiResourcePath") private String apiResourcePath;
    @Column(name = "apiResourceTemplate") private String apiResourceTemplate;
    @Column(name = "apiMethod") private String apiMethod;
    @Column(name = "apiCreator") private String apiCreator;
    @Column(name = "apiCreatorTenantDomain") private String apiCreatorTenantDomain;
    @Column(name = "apiTier") private String apiTier;
    @Column(name = "apiHostname") private String apiHostname;
    @Column(name = "username") private String username;
    @Column(name = "userTenantDomain") private String userTenantDomain;
    @Column(name = "userIp") private String userIp;
    @Column(name = "userAgent") private String userAgent;
    @Column(name = "throttledOut") private Integer throttledOut;
    @Column(name = "responseTime") private Long responseTime;
    @Column(name = "serviceTime") private Long serviceTime;
    @Column(name = "backendTime") private Long backendTime;
    @Column(name = "responseCacheHit") private Integer responseCacheHit;
    @Column(name = "responseSize") private Long responseSize;
    @Column(name = "protocol") private String protocol;
    @Column(name = "responseCode") private Integer responseCode;
    @Column(name = "securityLatency") private String securityLatency;
    @Column(name = "destination") private String destination;
    @Column(name = "throttlingLatency") private Long throttlingLatency;
    @Column(name = "requestMedLat") private Long requestMedLat;
    @Column(name = "responseMedLat") private Long responseMedLat;
    @Column(name = "backendLatency") private Long backendLatency;
    @Column(name = "otherLatency") private Long otherLatency;
    @Column(name = "gatewayType") private String gatewayType;
    @Column(name = "label") private String label;
    @Column(name = "properties") private String properties;
    @Column(name = "providerbillid") private Long providerbillid;
    @Column(name = "aggregatorbillid") private Long aggregatorbillid;
}