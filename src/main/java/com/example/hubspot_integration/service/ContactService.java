package com.example.hubspot_integration.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hubspot_integration.client.HubspotApiClient;
import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.dto.ContactRequest;

@Service
public class ContactService {

    private final HubspotApiClient hubspotApiClient;
    private final HubspotOAuthProperties hubspotProps;

    public ContactService(HubspotApiClient hubspotApiClient, HubspotOAuthProperties hubspotProps) {
        this.hubspotApiClient = hubspotApiClient;
        this.hubspotProps = hubspotProps;
    }

    public ResponseEntity<String> createContact(ContactRequest request) {
        String path = "/crm/v3/objects/contacts";
        Map<String, Object> contactBody = Map.of(
                "properties", Map.of(
                        "email", request.getEmail(),
                        "firstname", request.getFirstname(),
                        "lastname", request.getLastname()
                )
        );

        return hubspotApiClient.post(path, contactBody, String.class);
    }
}