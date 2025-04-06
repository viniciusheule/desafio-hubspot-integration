package com.example.hubspot_integration.exception;

public class HubspotApiException extends RuntimeException {
    public HubspotApiException(String message) {
        super(message);
    }

    public HubspotApiException(String message, Throwable cause) {
        super(message, cause);
    }
}