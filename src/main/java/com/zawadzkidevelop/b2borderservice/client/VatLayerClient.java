package com.zawadzkidevelop.b2borderservice.client;

import com.zawadzkidevelop.b2borderservice.configuration.ClientServiceErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class VatLayerClient {

    private final ClientServiceErrorHandler errorHandler;

    private final RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    @Autowired
    public VatLayerClient(ClientServiceErrorHandler errorHandler, RestTemplateBuilder restTemplateBuilder) {
        this.errorHandler = errorHandler;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @PostConstruct
    private void init() {
        restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(errorHandler);
    }

    public ResponseEntity callApiForObject(String endpointUrl, HttpMethod method, Class responseType) {
        return this.restTemplate.exchange(endpointUrl, method, HttpEntity.EMPTY, responseType);
    }

}
