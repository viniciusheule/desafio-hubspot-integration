package com.example.hubspot_integration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.hubspot_integration.service.OAuthService;

@Controller
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth/authorize")
    public String getAuthorizationUrl() {
        return "redirect:" + oAuthService.buildAuthorizationUrl();
    }

    @GetMapping("/oauth/callback")
    @ResponseBody
    public String callback(@RequestParam("code") String code) {
        String accessToken = oAuthService.exchangeCodeForAccessToken(code);
        return "Access Token: " + accessToken;
    }
}