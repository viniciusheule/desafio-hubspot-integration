package com.example.hubspot_integration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.hubspot_integration.config.HubspotProperties;

@Service
public class OAuthService {

    private final HubspotProperties properties;

    public OAuthService(HubspotProperties properties) {
        this.properties = properties;
    }

    public String buildAuthorizationUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("app.hubspot.com")
                .path("/oauth/authorize")
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("scope", properties.getScopes())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }
}