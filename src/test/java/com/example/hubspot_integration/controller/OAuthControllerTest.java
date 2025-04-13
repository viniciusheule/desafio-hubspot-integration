package com.example.hubspot_integration.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.hubspot_integration.config.NoSecurityConfig;
import com.example.hubspot_integration.service.OAuthService;

@WebMvcTest(OAuthController.class)
@Import(NoSecurityConfig.class)
@DisplayName("Testes para o controlador OAuth")
public class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OAuthService oAuthService;

    @Test
    @DisplayName("Deve redirecionar para a URL de autorização quando /oauth/authorize é acessado")
    void shouldRedirectToAuthorizationUrl() throws Exception {
        // Arrange (Preparar)
        String mockAuthUrl = "https://mock-authorization-url";
        when(oAuthService.buildAuthorizationUrl(mockAuthUrl)).thenReturn(mockAuthUrl);

        // Act (Agir)
        mockMvc.perform(get("/oauth/authorize"))
                // Assert (Verificar)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(mockAuthUrl));

        verify(oAuthService, times(1)).buildAuthorizationUrl(null); // Verificar se o método do mock foi chamado
    }

    @Test
    @DisplayName("Deve retornar o access token quando /oauth/callback é acessado com o código")
    void shouldReturnAccessTokenOnCallback() throws Exception {
        // Arrange (Preparar)
        String testCode = "test-code";
        String fakeAccessToken = "fake-access-token";
        when(oAuthService.exchangeCodeForAccessToken(testCode)).thenReturn(fakeAccessToken);

        // Act (Agir)
        mockMvc.perform(get("/oauth/callback").param("code", testCode))
                // Assert (Verificar)
                .andExpect(status().isOk())
                .andExpect(content().string("Access Token: " + fakeAccessToken));

        verify(oAuthService, times(1)).exchangeCodeForAccessToken(testCode); // Verificar se o método do mock foi chamado
    }

    // Outros testes podem ser adicionados aqui para cobrir diferentes cenários,
    // como a ausência do parâmetro 'code' no callback, ou cenários de erro no serviço.
}