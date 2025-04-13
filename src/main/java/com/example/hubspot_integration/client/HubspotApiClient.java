package com.example.hubspot_integration.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.exception.HubspotApiException;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Component
public class HubspotApiClient {

    private static final Logger logger = LoggerFactory.getLogger(HubspotApiClient.class);

    private final RestTemplate restTemplate;
    private final HubspotOAuthProperties hubspotProps;
    private final InMemoryTokenStore tokenStore;

    public HubspotApiClient(RestTemplate restTemplate, HubspotOAuthProperties hubspotProps, InMemoryTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.hubspotProps = hubspotProps;
        this.tokenStore = tokenStore;
    }

    public <T, R> ResponseEntity<R> post(String path, T body, Class<R> responseType) {
        String accessToken = tokenStore.getAccessToken();
        if (accessToken == null) {
            throw new HubspotApiException("Token de acesso ausente ou expirado.");
        }

        String url = hubspotProps.getApiBaseUrl() + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<>(body, headers);

        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                ResponseEntity<R> response = restTemplate.postForEntity(url, entity, responseType);
                if (response.getStatusCode() != HttpStatus.TOO_MANY_REQUESTS) {
                    return response;
                }

                retryCount++;
                long waitTime = 2000L;
                String retryAfter = response.getHeaders().getFirst("Retry-After");
                if (retryAfter != null) {
                    try {
                        waitTime = Long.parseLong(retryAfter) * 1000L;
                    } catch (NumberFormatException ignored) {
                    }
                }
                logger.warn("Recebido status 429. Tentando novamente em {} ms (tentativa {} de {}).", waitTime, retryCount, maxRetries);
                Thread.sleep(waitTime);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new HubspotApiException("Thread interrompida durante espera de retry.", e);
            } catch (Exception ex) {
                throw new HubspotApiException("Erro ao comunicar com a API do HubSpot: " + ex.getMessage(), ex);
            }
        }

        throw new HubspotApiException("Limite de tentativas excedido ao chamar a API do HubSpot.");
    }
}