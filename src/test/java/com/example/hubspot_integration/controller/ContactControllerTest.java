package com.example.hubspot_integration.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.hubspot_integration.dto.ContactRequest;
import com.example.hubspot_integration.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createContact_shouldReturnSuccess() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setEmail("teste@example.com");
        request.setFirstname("João");
        request.setLastname("Silva");

        when(contactService.createContact(any())).thenReturn(ResponseEntity.ok("Contato criado com sucesso"));

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contato criado com sucesso"));
    }

    @Test
    void createContact_shouldFailValidation() throws Exception {
        ContactRequest request = new ContactRequest(); // campos vazios

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }
}