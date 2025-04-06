package com.example.hubspot_integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.model.TokenResponse;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Service
public class OAuthService {

    private final RestTemplate restTemplate;
    private final HubspotOAuthProperties hubspotProps;
    private final InMemoryTokenStore tokenStore;

    @Autowired
    public OAuthService(RestTemplate restTemplate,
                        HubspotOAuthProperties hubspotProps,
                        InMemoryTokenStore tokenStore) { // <-- AQUI
        this.restTemplate = restTemplate;
        this.hubspotProps = hubspotProps;
        this.tokenStore = tokenStore; // <-- AQUI
    }

    public String buildAuthorizationUrl() {
        return hubspotProps.getAuthUrl()
                + "?client_id=" + hubspotProps.getClientId()
                + "&redirect_uri=" + hubspotProps.getRedirectUri()
                + "&scope=" + hubspotProps.getScopes().replace(" ", "%20")
                + "&response_type=code";
    }

    public String exchangeCodeForAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", hubspotProps.getClientId());
        form.add("client_secret", hubspotProps.getClientSecret());
        form.add("redirect_uri", hubspotProps.getRedirectUri());
        form.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                hubspotProps.getTokenUrl(),
                HttpMethod.POST,
                request,
                TokenResponse.class
        );

        TokenResponse tokenResponse = response.getBody();

        if (tokenResponse != null) {
            tokenStore.update(tokenResponse);
            return tokenResponse.getAccess_token();
        }

        return null;
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", hubspotProps.getClientId());
        form.add("client_secret", hubspotProps.getClientSecret());
        form.add("refresh_token", refreshToken);
    
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
    
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                hubspotProps.getTokenUrl(),
                HttpMethod.POST,
                request,
                TokenResponse.class
        );
    
        TokenResponse tokenResponse = response.getBody();

        if (tokenResponse != null) {
            tokenStore.update(tokenResponse);
        }

        return tokenResponse;
    }
}