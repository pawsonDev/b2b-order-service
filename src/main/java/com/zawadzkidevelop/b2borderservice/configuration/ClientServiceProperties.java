package com.zawadzkidevelop.b2borderservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "client")
public class ClientServiceProperties {

    @NotNull
    private String vatLayerServiceUri;

    @NotNull
    private String vatLayerAccessKey;
}
