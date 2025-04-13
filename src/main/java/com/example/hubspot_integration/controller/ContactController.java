package com.example.hubspot_integration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hubspot_integration.dto.ContactRequest;
import com.example.hubspot_integration.service.ContactService;

import jakarta.validation.Valid; // Import para a anotação @Valid

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactRequest request) {
        logger.info("Recebida requisição para criar um novo contato: {}", request);
        ResponseEntity<String> response = contactService.createContact(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Contato criado com sucesso. Status: {}", response.getStatusCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()); // Retornar 201 Created
        } else {
            logger.error("Falha ao criar contato. Status: {}, Corpo: {}", response.getStatusCode(), response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody()); // Replicar o status de erro
        }
    }
}