package com.example.hubspot_integration.store;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.hubspot_integration.model.TokenResponse;

@Component
public class InMemoryTokenStore {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryTokenStore.class);

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    public void update(TokenResponse tokenResponse) {
        this.accessToken = tokenResponse.getAccessToken();
        this.refreshToken = tokenResponse.getRefreshToken();
        this.expiresAt = Instant.now().plusSeconds(tokenResponse.getExpiresIn());
        logger.info("Tokens atualizados. Access Token: {}, Refresh Token: {}, Expira em: {}",
                    truncate(accessToken), truncate(refreshToken), expiresAt);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getExpiryDateTime() {
        return expiresAt;
    }

    public boolean isExpired() {
        boolean expired = expiresAt == null || Instant.now().isAfter(expiresAt);
        logger.debug("Verificação de expiração. Expira em: {}, Expirado: {}", expiresAt, expired);
        return expired;
    }

    // Método utilitário para truncar tokens para logging (evitar logar informações sensíveis completas)
    private String truncate(String token) {
        if (token != null && token.length() > 10) {
            return token.substring(0, 5) + "... (length: " + token.length() + ")";
        }
        return token;
    }
}