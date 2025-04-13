package com.example.hubspot_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Configurações relacionadas à autenticação OAuth com o HubSpot.
 * As propriedades são mapeadas do prefixo 'hubspot' no arquivo de configuração (ex: application.properties).
 */
@Data
@ConfigurationProperties(prefix = "hubspot")
public class HubspotOAuthProperties {

    /**
     * O ID do cliente da sua aplicação HubSpot.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "O clientId do HubSpot deve ser configurado.")
    private String clientId;

    /**
     * O segredo do cliente da sua aplicação HubSpot.
     * Esta propriedade é obrigatória e confidencial.
     */
    @NotBlank(message = "O clientSecret do HubSpot deve ser configurado.")
    private String clientSecret;

    /**
     * O URI de redirecionamento configurado na sua aplicação HubSpot.
     * Este URI deve corresponder ao endpoint de callback da sua aplicação.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "O redirectUri do HubSpot deve ser configurado.")
    private String redirectUri;

    /**
     * Os escopos (permissões) solicitados para o access token.
     * Os escopos devem ser separados por espaços.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "Os scopes do HubSpot devem ser configurados.")
    private String scopes;

    /**
     * A URL de autorização do HubSpot.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "A authUrl do HubSpot deve ser configurada.")
    private String authUrl;

    /**
     * A URL para trocar o código de autorização por um token de acesso.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "A tokenUrl do HubSpot deve ser configurada.")
    private String tokenUrl;

    /**
     * A URL base da API do HubSpot.
     * Esta propriedade é obrigatória.
     */
    @NotBlank(message = "A apiBaseUrl do HubSpot deve ser configurada.")
    private String apiBaseUrl;

    /**
     * Intervalo em milissegundos para verificar e renovar o token de acesso.
     * Usado pelo agendador de refresh de token.
     */
    private long oauthRefreshIntervalMs = 60000; // Valor padrão de 1 minuto
}