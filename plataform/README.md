
# 🧠 Estrutura de Pastas - Projeto iCar Platform

Este projeto foi estruturado com base na arquitetura **hexagonal (Ports and Adapters)**, com forte inspiração na **Clean Architecture**. O objetivo é promover **separação de responsabilidades**, **baixo acoplamento** e **alta coesão** entre as camadas, facilitando a manutenção, testes e escalabilidade da aplicação.

---

## 📁 plataform/src/main/java/com/icar/plataform

### 📄 PlataformApplication.java
Classe principal da aplicação Spring Boot. É o ponto de entrada da aplicação.

---

## 🔌 Camada de Interface (API)

### 📁 api

Contém tudo relacionado à interface de comunicação com o mundo externo (normalmente via REST API).

- **controller/v1**: Camada de controle. Aqui ficam os controladores REST que recebem as requisições HTTP e repassam para os serviços.
- **dto/request**: Objetos usados para receber dados enviados pelo cliente (payload da requisição).
- **dto/response**: Objetos usados para enviar dados de resposta ao cliente.
- **mapper**: Responsável por converter entidades para DTOs e vice-versa, promovendo a separação entre as camadas internas e externas.

---

## 🧠 Camada de Aplicação

### 📁 application

Contém as regras de negócio e orquestração de fluxos da aplicação.

- **service**: Interfaces que definem os contratos dos serviços da aplicação.
- **serviceImpl**: Implementações concretas dos serviços definidos nas interfaces. É onde a lógica da aplicação acontece.
- **integration/payment**: Classes responsáveis pela integração com serviços externos (ex: gateways de pagamento como Stripe).

---

## ⚙️ Camada de Configuração

### 📁 config

Contém classes de configuração da aplicação, como segurança, documentação da API, CORS, etc.

- **SecurityConfig.java**: Configurações de segurança (Spring Security, JWT, etc).
- **OpenAPIConfig.java**: Configuração da documentação da API via Swagger/OpenAPI.

---

## 🧩 Camada de Domínio

### 📁 domain

Responsável pelas regras e estrutura do domínio do negócio.

- **enums**: Enumerações utilizadas nas entidades ou lógicas de negócio (ex: status de agendamento, métodos de pagamento).
- **model**: Entidades da aplicação (representam tabelas do banco de dados ou conceitos do domínio).
- **repository**: Interfaces para acesso ao banco de dados, usando Spring Data JPA.

---

## 🛠️ Camada de Infraestrutura

### 📁 infrastructure

Responsável por validações e regras auxiliares que suportam o domínio e a aplicação.

- **validation/exception**: Exceções específicas usadas durante processos de validação.
- **validation/validator**: Classes de validação personalizada que verificam regras específicas de negócio ou dados antes de persistência.

---

## ♻️ Componentes Compartilhados

### 📁 shared

Contém classes reutilizáveis por toda a aplicação.

- **exception**: Exceções genéricas e tratadores globais de erro (ex: `BusinessException`, `GlobalExceptionHandler`, etc).

---

## ✅ Boas práticas

- Use `dto` apenas para entrada e saída de dados da API. Nunca expose diretamente entidades.
- Toda regra de negócio deve estar nas camadas `service` e `serviceImpl`.
- Controladores (`controller`) não devem conter lógica, apenas orquestrar os serviços.
- Validadores devem ser reutilizáveis e focados em apenas uma responsabilidade.

---

Esse modelo facilita a escalabilidade da aplicação, permite testes unitários mais eficazes e desacopla as dependências externas do núcleo do domínio.
