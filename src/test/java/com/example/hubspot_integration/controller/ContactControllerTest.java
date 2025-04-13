package com.example.hubspot_integration.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.hubspot_integration.dto.ContactRequest;
import com.example.hubspot_integration.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ContactController.class)
@DisplayName("Testes para o controlador de Contatos")
class ContactControllerTest {

    private static final String CONTACTS_ENDPOINT = "/contacts";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar status 200 OK e a resposta do serviço ao criar um contato com sucesso")
    void createContact_shouldReturnSuccess() throws Exception {
        // Arrange
        ContactRequest request = new ContactRequest();
        request.setEmail("teste@example.com");
        request.setFirstname("João");
        request.setLastname("Silva");
        String expectedResponse = "Contato criado com sucesso";

        when(contactService.createContact(any())).thenReturn(ResponseEntity.ok(expectedResponse));

        // Act & Assert
        mockMvc.perform(post(CONTACTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(contactService, times(1)).createContact(any()); // Verifica se o serviço foi chamado
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request e detalhes dos erros de validação ao tentar criar um contato com dados inválidos")
    void createContact_shouldFailValidation() throws Exception {
        // Arrange
        ContactRequest request = new ContactRequest(); // Campos vazios (inválidos)

        // Act & Assert
        mockMvc.perform(post(CONTACTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());

        verify(contactService, never()).createContact(any()); // Verifica que o serviço NÃO foi chamado
    }

    @Test
    @DisplayName("Deve retornar o status retornado pelo serviço em caso de falha na criação do contato")
    void createContact_shouldReturnServiceErrorStatus() throws Exception {
        // Arrange
        ContactRequest request = new ContactRequest();
        request.setEmail("error@example.com");
        request.setFirstname("Erro");
        request.setLastname("Teste");
        String errorMessage = "Erro ao criar contato no HubSpot";

        when(contactService.createContact(any())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage));

        // Act & Assert
        mockMvc.perform(post(CONTACTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errorMessage));

        verify(contactService, times(1)).createContact(any());
    }
}