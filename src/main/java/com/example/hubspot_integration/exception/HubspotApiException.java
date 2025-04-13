package com.example.hubspot_integration.exception;

public class HubspotApiException extends RuntimeException {

    private Integer hubspotStatusCode; // Código de status HTTP retornado pela API do HubSpot (opcional)
    private String hubspotErrorMessage; // Mensagem de erro específica da API do HubSpot (opcional)

    public HubspotApiException(String message) {
        super(message);
    }

    public HubspotApiException(String message, Throwable cause) {
        super(message, cause);
    }

    // Construtor com informações adicionais da API do HubSpot (opcional)
    public HubspotApiException(String message, Integer hubspotStatusCode, String hubspotErrorMessage) {
        super(message);
        this.hubspotStatusCode = hubspotStatusCode;
        this.hubspotErrorMessage = hubspotErrorMessage;
    }

    public HubspotApiException(String message, Throwable cause, Integer hubspotStatusCode, String hubspotErrorMessage) {
        super(message, cause);
        this.hubspotStatusCode = hubspotStatusCode;
        this.hubspotErrorMessage = hubspotErrorMessage;
    }

    public Integer getHubspotStatusCode() {
        return hubspotStatusCode;
    }

    public String getHubspotErrorMessage() {
        return hubspotErrorMessage;
    }
}