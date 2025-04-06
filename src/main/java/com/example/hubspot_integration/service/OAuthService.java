package com.example.hubspot_integration.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.hubspot_integration.config.HubspotProperties;
import com.example.hubspot_integration.dto.AccessTokenResponse;

@Service
public class OAuthService {

    private final HubspotProperties properties;
    private final RestTemplate restTemplate;

    public OAuthService(HubspotProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
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

    @SuppressWarnings("null")
    public String exchangeCodeForAccessToken(String code) {
        String tokenUrl = properties.getTokenUrl();
    
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
    
        ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(
            tokenUrl, request, AccessTokenResponse.class);
    
        return response.getBody().getAccessToken();
    }
}