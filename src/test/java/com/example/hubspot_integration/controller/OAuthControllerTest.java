package com.example.hubspot_integration.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OAuthService oAuthService;

    @Test
    void shouldRedirectToAuthorizationUrl() throws Exception {
        when(oAuthService.buildAuthorizationUrl())
                .thenReturn("https://mock-authorization-url");

        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://mock-authorization-url"));
    }

    @Test
    void shouldReturnAccessTokenOnCallback() throws Exception {
        when(oAuthService.exchangeCodeForAccessToken("test-code"))
                .thenReturn("fake-access-token");

        mockMvc.perform(get("/oauth/callback").param("code", "test-code"))
                .andExpect(status().isOk())
                .andExpect(content().string("Access Token: fake-access-token"));
    }
}