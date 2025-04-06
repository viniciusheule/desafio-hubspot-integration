package com.example.hubspot_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "hubspot")
public class HubspotOAuthProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;
    private String authUrl;
    private String tokenUrl;
    private String apiBaseUrl;
}