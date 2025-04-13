package com.example.hubspot_integration.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.hubspot_integration.service.OAuthService;

import jakarta.servlet.http.HttpSession; // Import HttpSession

@Controller
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth/authorize")
    public RedirectView getAuthorizationUrl(HttpSession session) {
        // 1. Gerar um valor aleatório para o 'state'
        String state = UUID.randomUUID().toString();

        // 2. Armazenar o 'state' na sessão do usuário
        session.setAttribute("oauth_state", state);

        // 3. Construir a URL de autorização incluindo o 'state'
        String authorizationUrl = oAuthService.buildAuthorizationUrl(state);

        // Redirecionar o usuário para a URL de autorização
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/oauth/callback")
    @ResponseBody
    public String callback(@RequestParam("code") String code,
                           @RequestParam(value = "state", required = false) String receivedState,
                           HttpSession session) {
        // 1. Recuperar o 'state' armazenado na sessão
        String storedState = (String) session.getAttribute("oauth_state");

        // 2. Verificar se o 'state' foi recebido e se corresponde ao armazenado
        if (receivedState == null || !receivedState.equals(storedState)) {
            // Limpar o 'state' da sessão (em caso de falha também)
            session.removeAttribute("oauth_state");
            return "Erro: Falha na verificação do 'state' (possível ataque CSRF).";
        }

        // 3. Se o 'state' corresponder, prosseguir com a troca do código pelo access token
        String accessToken = oAuthService.exchangeCodeForAccessToken(code);

        // 4. Limpar o 'state' da sessão após o sucesso
        session.removeAttribute("oauth_state");

        return "Access Token: " + accessToken;
    }
}