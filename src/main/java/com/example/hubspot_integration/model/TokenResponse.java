package com.example.hubspot_integration.model;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String token_type;
}
