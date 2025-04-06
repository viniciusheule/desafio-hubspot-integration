package com.example.hubspot_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.hubspot_integration.config.HubspotOAuthProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(HubspotOAuthProperties.class)
public class HubspotIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubspotIntegrationApplication.class, args);
	}

}
