package com.example.hubspot_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.hubspot_integration.config.HubspotProperties;

@SpringBootApplication
@EnableConfigurationProperties(HubspotProperties.class)
public class HubspotIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubspotIntegrationApplication.class, args);
	}

}
