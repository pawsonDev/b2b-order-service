package com.zawadzkidevelop.b2borderservice.service;

import com.zawadzkidevelop.b2borderservice.client.VatLayerClient;
import com.zawadzkidevelop.b2borderservice.configuration.ClientServiceProperties;
import com.zawadzkidevelop.b2borderservice.model.dto.VatNumberSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VatNumberValidationService {

    private final VatLayerClient vatLayerClient;

    private final ClientServiceProperties clientServiceProperties;

    @Autowired
    public VatNumberValidationService(VatLayerClient vatLayerClient, ClientServiceProperties clientServiceProperties) {
        this.vatLayerClient = vatLayerClient;
        this.clientServiceProperties = clientServiceProperties;
    }

    public boolean validateVatNumber(String vatNumber) {
        ResponseEntity<VatNumberSpecification> response = vatLayerClient.callApiForObject(getValidationEndpointUrl(vatNumber), HttpMethod.GET, VatNumberSpecification.class);
        if (response != null && response.hasBody() && response.getBody().isValid()) {
            log.info("VAT number validation status: {}  - VALID", vatNumber);
            return true;
        }
        log.info("VAT number validation status: {}  - NOT VALID", vatNumber);
        return false;
    }

    private String getValidationEndpointUrl(String vatNumber) {
        StringBuilder endpointUrl = new StringBuilder();
        endpointUrl.append(clientServiceProperties.getVatLayerServiceUri());
        endpointUrl.append("/validate?access_key=").append(clientServiceProperties.getVatLayerAccessKey());
        endpointUrl.append("&vat_number=").append(vatNumber);
        return endpointUrl.toString();
    }
}
