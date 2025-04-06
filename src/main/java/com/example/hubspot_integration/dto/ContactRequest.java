package com.example.hubspot_integration.dto;

import lombok.Data;

@Data
public class ContactRequest {
    private String email;
    private String firstname;
    private String lastname;
}