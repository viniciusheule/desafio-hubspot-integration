package com.example.hubspot_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "hubspot")
@Component
public class HubspotProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;
    private String authUrl;
    private String tokenUrl;
    private String apiBaseUrl;
}