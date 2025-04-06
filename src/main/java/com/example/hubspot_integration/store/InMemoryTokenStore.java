package com.example.hubspot_integration.store;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.example.hubspot_integration.model.TokenResponse;

@Component
public class InMemoryTokenStore {

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    public void update(TokenResponse tokenResponse) {
        this.accessToken = tokenResponse.getAccess_token();
        this.refreshToken = tokenResponse.getRefresh_token();
        this.expiresAt = Instant.now().plusSeconds(tokenResponse.getExpires_in());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }
}