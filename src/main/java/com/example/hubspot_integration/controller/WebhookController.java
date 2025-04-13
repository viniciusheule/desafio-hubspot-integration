package com.example.hubspot_integration.controller;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hubspot_integration.dto.WebhookEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Value("${hubspot.client-secret}")
    private String clientSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isValidSignatureV2(String signature, String requestBody, String requestMethod, String requestUri) {
        try {
            String data = requestMethod + requestUri + requestBody;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = bytesToHex(digest);
            return signature.equals(calculatedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Erro ao validar a assinatura v2 do webhook: {}", e.getMessage());
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @PostMapping("/contact")
    public ResponseEntity<String> handleContactWebhook(
            @RequestHeader("X-HubSpot-Signature-v2") String signatureV2,
            @RequestBody List<WebhookEvent> events,
            org.springframework.http.HttpRequest request // Para acessar o método e URI
    ) {
        try {
            String requestBody = objectMapper.writeValueAsString(events);
            String requestMethod = request.getMethod().name();
            String requestUri = request.getURI().getPath();

            if (!isValidSignatureV2(signatureV2, requestBody, requestMethod, requestUri)) {
                logger.warn("Falha na validação da assinatura v2 do webhook.");
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            for (WebhookEvent event : events) {
                if ("contact.creation".equals(event.getSubscriptionType())) {
                    logger.info("Novo contato criado - ID: {}, Email: {}",
                            event.getObjectId(),
                            event.getProperties().getOrDefault("email", "desconhecido"));
                } else {
                    logger.warn("Evento não suportado: {}", event.getSubscriptionType());
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Erro ao processar o webhook de contato: {}", e.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}