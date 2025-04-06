package com.example.hubspot_integration.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hubspot_integration.dto.WebhookEvent;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/contact")
    public void handleContactWebhook(@RequestBody List<WebhookEvent> events) {
        for (WebhookEvent event : events) {
            if ("contact.creation".equals(event.getSubscriptionType())) {
                logger.info("Novo contato criado - ID: {}, Email: {}",
                        event.getObjectId(),
                        event.getProperties().getOrDefault("email", "desconhecido"));
            } else {
                logger.warn("Evento não suportado: {}", event.getSubscriptionType());
            }
        }
    }
}