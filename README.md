# HubSpot Integration API

API REST em Java com Spring Boot para integração com a API do HubSpot, utilizando OAuth 2.0.

## Funcionalidades

- Geração da URL de autorização OAuth
- Processamento do callback OAuth (troca de code por access token)
- Criação de contatos no CRM do HubSpot
- Recebimento de webhooks de criação de contatos
- Renovação automática de tokens com `refresh_token`
- Tratamento de rate limit com retry automático
- Tratamento de erros com exceções customizadas
- Documentação Swagger (opcional)

---

## Requisitos

- Java 17+
- Maven 3.8+
- Conta HubSpot com app OAuth 2.0 configurado
- Token de desenvolvedor e escopos: `crm.schemas.contacts.read`, `crm.schemas.contacts.write`

---

## Configuração

### 1. Clonar o repositório

git clone https://github.com/seu-usuario/hubspot-integration.git
cd hubspot-integration

### 2. Configurar o application.yml

server:
  port: 8080

hubspot:
  client-id: SEU_CLIENT_ID
  client-secret: SEU_CLIENT_SECRET
  redirect-uri: http://localhost:8080/oauth/callback
  scopes: crm.schemas.contacts.read crm.schemas.contacts.write
  auth-url: https://app.hubspot.com/oauth/authorize
  token-url: https://api.hubapi.com/oauth/v1/token
  api-base-url: https://api.hubapi.com

---

## Executando a aplicação

via terminal maven

Linux
./mvnw spring-boot:run
ou
Windows
mvnw spring-boot:run

---

## Endpoints

1) Geração da URL de autorização

GET /oauth/authorize

-> Redireciona para a tela de autorização do HubSpot.

2) Callback OAuth

GET /oauth/callback?code={code}

-> Recebe o code e troca por access_token e refresh_token.

3) Criação de Contato

POST /contacts
Content-Type: application/json

{
  "email": "exemplo@email.com",
  "firstname": "Nome",
  "lastname": "Sobrenome"
}

4) Webhook de criação de contatos

POST /webhook/contact
Content-Type: application/json

-> Recebe eventos de "contact.creation" enviados pelo HubSpot.