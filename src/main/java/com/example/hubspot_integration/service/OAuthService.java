package com.example.hubspot_integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.exception.HubspotApiException;
import com.example.hubspot_integration.model.TokenResponse;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Service
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    private final RestTemplate restTemplate;
    private final HubspotOAuthProperties hubspotProps;
    private final InMemoryTokenStore tokenStore;

    public OAuthService(RestTemplate restTemplate,
                        HubspotOAuthProperties hubspotProps,
                        InMemoryTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.hubspotProps = hubspotProps;
        this.tokenStore = tokenStore;
    }

    public String buildAuthorizationUrl(String state) {
        return hubspotProps.getAuthUrl()
                + "?client_id=" + hubspotProps.getClientId()
                + "&redirect_uri=" + hubspotProps.getRedirectUri()
                + "&scope=" + hubspotProps.getScopes().replace(" ", "%20")
                + "&response_type=code"
                + "&state=" + state;
    }

    @SuppressWarnings("null")
    public String exchangeCodeForAccessToken(String authorizationCode) {
        logger.info("Iniciando troca de código por access token para o código: {}", authorizationCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
        form.add("client_id", hubspotProps.getClientId());
        form.add("client_secret", hubspotProps.getClientSecret());
        form.add("redirect_uri", hubspotProps.getRedirectUri());
        form.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    hubspotProps.getTokenUrl(),
                    HttpMethod.POST,
                    request,
                    TokenResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                tokenStore.update(tokenResponse);
                logger.info("Access token obtido e armazenado com sucesso.");
                return tokenResponse.getAccessToken();
            } else {
                logger.error("Falha ao trocar código por access token. Status: {}, Corpo: {}",
                             response.getStatusCode(), response.getBody());
                throw new HubspotApiException("Falha ao trocar código por access token. Status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Erro de comunicação ao trocar código por access token: {}", e.getMessage());
            throw new HubspotApiException("Erro de comunicação ao trocar código por access token: " + e.getMessage(), e);
        }
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        logger.info("Iniciando refresh do access token com refresh token: {}", refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GRANT_TYPE_REFRESH_TOKEN);
        form.add("client_id", hubspotProps.getClientId());
        form.add("client_secret", hubspotProps.getClientSecret());
        form.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    hubspotProps.getTokenUrl(),
                    HttpMethod.POST,
                    request,
                    TokenResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                tokenStore.update(tokenResponse);
                logger.info("Access token refreshed e armazenado com sucesso.");
                return tokenResponse;
            } else {
                logger.error("Falha ao refresh do access token. Status: {}, Corpo: {}",
                             response.getStatusCode(), response.getBody());
                throw new HubspotApiException("Falha ao refresh do access token. Status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Erro de comunicação ao refresh do access token: {}", e.getMessage());
            throw new HubspotApiException("Erro de comunicação ao refresh do access token: " + e.getMessage(), e);
        }
    }
}