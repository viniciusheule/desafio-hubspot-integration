package com.example.hubspot_integration.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.hubspot_integration.service.OAuthService;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Component
public class TokenRefreshScheduler {

    private final OAuthService oAuthService;
    private final InMemoryTokenStore tokenStore;

    public TokenRefreshScheduler(OAuthService oAuthService, InMemoryTokenStore tokenStore) {
        this.oAuthService = oAuthService;
        this.tokenStore = tokenStore;
    }

    @Scheduled(fixedDelay = 60 * 1000) // verifica a cada 1 min
    public void refreshTokenIfNeeded() {
        if (tokenStore.getRefreshToken() != null && tokenStore.isExpired()) {
            System.out.println("Token expirado. Renovando...");
            oAuthService.refreshAccessToken(tokenStore.getRefreshToken());
        }
    }
}