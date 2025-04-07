package com.example.hubspot_integration.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.dto.ContactRequest;
import com.example.hubspot_integration.exception.HubspotApiException;
import com.example.hubspot_integration.exception.UnauthorizedException;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Service
public class ContactService {

    private final RestTemplate restTemplate;
    private final HubspotOAuthProperties hubspotProps;
    private final InMemoryTokenStore tokenStore;

    
    public ContactService(RestTemplate restTemplate, HubspotOAuthProperties hubspotProps, InMemoryTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.hubspotProps = hubspotProps;
        this.tokenStore = tokenStore;
    }

    public ResponseEntity<String> createContact(ContactRequest request){
    String accessToken = tokenStore.getAccessToken();

        if (accessToken == null) {
            throw new UnauthorizedException("Token de acesso ausente ou expirado.");
        }

        String url = hubspotProps.getApiBaseUrl() + "/crm/v3/objects/contacts";
        
        Map<String, Object> contactBody = Map.of(
            "properties", Map.of(
                "email", request.getEmail(),
                "firstname", request.getFirstname(),
                "lastname", request.getLastname()
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contactBody, headers);

        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

                if (response.getStatusCode() != HttpStatus.TOO_MANY_REQUESTS) {
                    return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
                }

                retryCount++;
                long waitTime = 2000L;
                String retryAfter = response.getHeaders().getFirst("Retry-After");

                if (retryAfter != null) {
                    try {
                        waitTime = Long.parseLong(retryAfter) * 1000L;
                    } catch (NumberFormatException ignored) {}
                }

                Thread.sleep(waitTime);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new HubspotApiException("Thread interrompida durante espera de retry.", e);
            } catch (Exception ex) {
                throw new HubspotApiException("Erro ao criar contato: " + ex.getMessage(), ex);
            }
        }

        throw new HubspotApiException("Limite de tentativas excedido. Tente novamente mais tarde.");
    }
}