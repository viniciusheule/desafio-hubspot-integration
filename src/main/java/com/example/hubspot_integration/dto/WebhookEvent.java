package com.example.hubspot_integration.dto;

import java.util.Map;

import lombok.Data;

/**
 * Representa um evento recebido através do webhook do HubSpot.
 */
@Data
public class WebhookEvent {

    /**
     * O ID único deste evento de webhook.
     */
    private Long eventId;

    /**
     * O ID da assinatura do webhook que gerou este evento.
     */
    private Long subscriptionId;

    /**
     * O ID do portal HubSpot associado ao evento.
     */
    private Long portalId;

    /**
     * O ID da aplicação HubSpot que configurou a assinatura do webhook.
     */
    private Long appId;

    /**
     * O timestamp em milissegundos em que o evento ocorreu no HubSpot.
     */
    private Long occurredAt;

    /**
     * O tipo da assinatura do webhook (ex: "contact.creation", "company.deletion").
     */
    private String subscriptionType;

    /**
     * O ID do objeto HubSpot envolvido no evento (ex: ID de um contato, ID de uma empresa).
     */
    private Long objectId;

    /**
     * Um mapa contendo as propriedades do objeto HubSpot no momento do evento.
     * As chaves são os nomes das propriedades e os valores são suas representações em String.
     */
    private Map<String, String> properties;
}