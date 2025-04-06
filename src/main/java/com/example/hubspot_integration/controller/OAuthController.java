package com.example.hubspot_integration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hubspot_integration.service.OAuthService;

@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth/authorize")
    public String getAuthorizationUrl() {
        return oAuthService.buildAuthorizationUrl();
    }
}