package com.example.hubspot_integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Representa a resposta da API do HubSpot ao solicitar um access token (e refresh token).
 */
@Data
public class AccessTokenResponse {

    /**
     * O token de acesso que pode ser usado para fazer chamadas autenticadas à API do HubSpot.
     * Corresponde ao campo "access_token" na resposta JSON.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * O token de refresh que pode ser usado para obter um novo access token quando o atual expirar.
     * Corresponde ao campo "refresh_token" na resposta JSON.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * O tempo em segundos até que o access token expire.
     * Corresponde ao campo "expires_in" na resposta JSON.
     */
    @JsonProperty("expires_in")
    private int expiresIn;

    /**
     * Os escopos (permissões) concedidos ao access token.
     * Corresponde ao campo "scope" na resposta JSON.
     */
    private String scope;

    /**
     * O tipo do token (geralmente "bearer").
     * Corresponde ao campo "token_type" na resposta JSON.
     */
    @JsonProperty("token_type")
    private String tokenType;
}