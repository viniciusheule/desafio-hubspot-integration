package com.example.hubspot_integration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.hubspot_integration.service.OAuthService;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    //Gera e redireciona para a URL de autorização
    @GetMapping("/authorize")
    public RedirectView redirectToAuthorization() {
        String url = oAuthService.buildAuthorizationUrl();
        return new RedirectView(url);
    }

    //Recebe o 'code' do HubSpot após o login e troca pelo access_token
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        String accessToken = oAuthService.exchangeCodeForAccessToken(code);
        return ResponseEntity.ok("Access Token: " + accessToken);
    }
}
