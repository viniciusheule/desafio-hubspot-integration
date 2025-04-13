package com.example.hubspot_integration.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.hubspot_integration.model.TokenResponse;
import com.example.hubspot_integration.service.OAuthService;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@Component
public class TokenRefreshScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TokenRefreshScheduler.class);

    private final OAuthService oAuthService;
    private final InMemoryTokenStore tokenStore;

    @Value("${hubspot.oauth.refresh-interval-ms}")
    private long refreshIntervalMs;

    public TokenRefreshScheduler(OAuthService oAuthService, InMemoryTokenStore tokenStore) {
        this.oAuthService = oAuthService;
        this.tokenStore = tokenStore;
    }

    @Scheduled(fixedDelayString = "${hubspot.oauth.refresh-interval-ms}")
    public void refreshTokenIfNeeded() {
        logger.info("Verificando se o token de acesso precisa ser renovado...");
        if (tokenStore.getRefreshToken() != null && tokenStore.isExpired()) {
            logger.warn("Token de acesso expirado. Iniciando processo de renovação...");
            try {
                TokenResponse newTokenResponse = oAuthService.refreshAccessToken(tokenStore.getRefreshToken());
                if (newTokenResponse != null) {
                    logger.info("Token de acesso renovado com sucesso. Novo token expira em: {}", tokenStore.getExpiryDateTime());
                } else {
                    logger.error("Falha ao renovar o token de acesso. Verifique as configurações do OAuth.");
                    // Aqui você pode adicionar lógica adicional, como desativar funcionalidades
                    // que dependem do token ou notificar um administrador.
                }
            } catch (Exception e) {
                logger.error("Ocorreu um erro durante a renovação do token de acesso: {}", e.getMessage());
                // Lide com a exceção conforme necessário (por exemplo, log mais detalhado, métricas).
            }
        } else if (tokenStore.getRefreshToken() == null) {
            logger.info("Refresh token não disponível. A renovação automática não pode ser realizada.");
        } else {
            logger.info("Token de acesso ainda é válido. Nenhuma renovação necessária. Expira em: {}", tokenStore.getExpiryDateTime());
        }
    }
}