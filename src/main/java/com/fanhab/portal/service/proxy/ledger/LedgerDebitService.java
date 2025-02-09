package com.fanhab.portal.service.proxy.ledger;

import com.fanhab.portal.config.GlobalProperties;
import com.fanhab.portal.dto.request.DebitDto;
import com.fanhab.portal.dto.response.DebitResponseDto;
import com.fanhab.portal.utils.SSLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service

public class LedgerDebitService {
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private GlobalProperties globalProperties;


    @Autowired
    public LedgerDebitService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public DebitResponseDto sendToDebit(DebitDto debitDto){
        if (globalProperties.getIsActiveTrustSSL()) {
            SSLUtil.trustAllCertificates();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DebitDto> entity = new HttpEntity<>(debitDto, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(globalProperties.getLedger_debit_account_url(), HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.getBody(), DebitResponseDto.class);
            } else {
                throw new RuntimeException("Failed to Post debit: HTTP status " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching users: " + e.getMessage(), e);
        }
    }


}
