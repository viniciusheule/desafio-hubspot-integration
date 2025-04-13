package com.example.hubspot_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Classe de configuração responsável por criar e configurar o bean RestTemplate
 * para realizar chamadas HTTP a serviços externos, como a API do HubSpot.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Cria e configura uma instância do RestTemplate, que é a classe principal
     * do Spring para realizar operações HTTP do lado do cliente.
     * Por padrão, o RestTemplate vem com configurações razoáveis, mas pode ser
     * personalizado conforme a necessidade (ex: timeouts, interceptadores).
     *
     * @return Uma instância configurada do RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        // Aqui você pode adicionar configurações personalizadas ao RestTemplate, se necessário.
        // Exemplo:
        // ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // ((HttpComponentsClientHttpRequestFactory) requestFactory).setConnectTimeout(5000); // Timeout de conexão
        // ((HttpComponentsClientHttpRequestFactory) requestFactory).setReadTimeout(10000);  // Timeout de leitura
        // RestTemplate restTemplate = new RestTemplate(requestFactory);
        // restTemplate.setInterceptors(...); // Adicionar interceptadores
        return new RestTemplate();
    }
}