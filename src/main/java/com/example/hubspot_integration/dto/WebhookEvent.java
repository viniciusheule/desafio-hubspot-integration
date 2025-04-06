package com.example.hubspot_integration.dto;

import java.util.Map;

import lombok.Data;

@Data
public class WebhookEvent {
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long appId;
    private Long occurredAt;
    private String subscriptionType;
    private Long objectId;
    private Map<String, String> properties;
}