package com.example.hubspot_integration.service;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.example.hubspot_integration.config.HubspotOAuthProperties;
import com.example.hubspot_integration.dto.ContactRequest;
import com.example.hubspot_integration.store.InMemoryTokenStore;

@SpringBootTest
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HubspotOAuthProperties hubspotProps;

    @Autowired
    private InMemoryTokenStore tokenStore;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        tokenStore.update(new com.example.hubspot_integration.model.TokenResponse() {{
            setAccessToken(getAccessToken());
            setExpiresIn(getExpiresIn());
            setRefreshToken("mock-refresh-token");
        }});
    }

    @Test
    void createContact_shouldReturnSuccess() {
        // Arrange
        ContactRequest request = new ContactRequest();
        request.setEmail("test@example.com");
        request.setFirstname("João");
        request.setLastname("Silva");

        String expectedUrl = hubspotProps.getApiBaseUrl() + "/crm/v3/objects/contacts";

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer mock-access-token"))
                .andRespond(withSuccess("{\"id\": \"123\"}", MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<?> response = contactService.createContact(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"id\": \"123\"}");

        mockServer.verify();
    }
}
