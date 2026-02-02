# UrbanCARD API

## Visão Geral

Esta API foi desenvolvida como parte de um teste técnico, seguindo boas práticas de arquitetura backend, segurança, separação de responsabilidades e padrão REST.

O sistema gerencia:

- Usuários  
- Cartões
- Transações de cartão
- Autenticação com JWT  
- Controle de acesso por roles (**USER / ADMIN**)

A aplicação foi construída com Spring Boot, utilizando PostgreSQL, JPA/Hibernate, Flyway e Spring Security.

---

## Arquitetura

A aplicação segue uma arquitetura em camadas:

```
Controller → Service → Repository → Database
```

---

## Estrutura de Pacotes

```
com.desafio.userapi
├── config        # Configurações de segurança e Swagger
├── controller    # Controllers REST (Auth, User, Card, Admin)
├── dto           # DTOs de entrada e saída
├── entity        # Entidades JPA
├── exception     # Exceptions customizadas e handler global
├── repository    # Repositórios JPA
├── security      # JWT, filtros e autenticação
└── service       # Regras de negócio
└── enums         # Enums de domínio (Role, TipoCartao, TransactionType)
```

---

## Autenticação & Segurança

- Autenticação baseada em **JWT**
- Sessão **stateless**
- Filtro JWT (`OncePerRequestFilter`)
- Controle de acesso por roles:
  - USER
  - ADMIN

### Regras de acesso

- Usuários **USER** 
  - Acessam apenas seus próprios dados
  - Gerenciam apenas seus próprios cartões
  - Visualizam apenas suas próprias transações
- Usuários **ADMIN** podem:
  - Visualizam todos os usuários
  - Visualizar e gerenciam cartões de qualquer usuário
  - Acesso administrativo completo

---

### Versionamento de Banco (Flyway)

- Banco versionado com Flyway
 - Migrations imutáveis (V1, V2, V3, V4…)
 - Criação automática de:
   - users
   - cards
   - card_transactions

- Seed automático de usuário ADMIN

Exemplo de migrations:

- V1__create_users_and_cards_tables.sql
- V2__create_admin_user.sql
- V3__create_card_transactions_table.sql
- V4__add_saldo_and_limite_to_cards.sql


---

## Endpoints Principais

### Autenticação

| Método | Endpoint        | Descrição |
|------|-----------------|-----------|
| POST | /auth/register  | Cadastro de usuário |
| POST | /auth/login     | Login e geração de token JWT |

### Usuário (USER)

| Método | Endpoint   | Descrição |
|------|------------|-----------|
| GET  | /users/me  | Dados do usuário autenticado |

### Cartões (USER)

| Método | Endpoint | Descrição |
|------|----------|-----------|
| POST | /cards | Cadastrar cartão |
| GET | /cards | Listar cartões do usuário |
| GET | /cards/{id}/transactions | Listar transações do cartão |
| PATCH | /cards/{id}/toggle | Ativar/Desativar cartão |
| DELETE | /cards/{id} | Remover cartão |

Cada transação registra: 
 - Tipo (CREDIT / DEBIT)
 - Valor
 - Saldo anterior
 - Saldo atual
 - Data da operação

**Validação de segurança:**  
O usuário não consegue alterar cartões que não sejam de sua posse.

---

## Administração (ADMIN)

| Método | Endpoint | Descrição |
|------|----------|-----------|
| GET | /admin/cards | Listar todos os cartões |
| GET | /admin/cards/user/{userId} | Cartões de um usuário |
| PATCH | /admin/cards/{id}/toggle | Ativar/Desativar qualquer cartão |
| DELETE | /admin/cards/{id} | Remover cartão |

---

## Swagger

A documentação da API está disponível em:

### Local

```
http://localhost:8080/swagger-ui.html
```

### Produção

```
https://urbanape-userapi.onrender.com/swagger-ui/index.html
```
- Possui botão **Authorize**
- Suporte a JWT no header `Authorization`
---

## Configuração

### application-dev.yml (exemplo)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/userdb
    username: postgres
    password: postgres

  flyway:
    enabled: true

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

jwt:
  secret: minha-chave-secreta-dev
```

---

## Como Executar

### Localmente

```bash
./mvnw spring-boot:run
```
- ou via IntelliJ (perfil dev ativo).

A aplicação estará disponível em:

```
http://localhost:8080
```

---

## Tratamento de Erros

 - Handler global de exceções
 - Respostas padronizadas
 - Mensagens claras para autenticação, autorização e validações

---

## Padrões e Boas Práticas

 - DTOs para entrada e saída
 - Exceptions customizadas
 - Controllers enxutos
 - Regra de negócio no Service
 - Segurança no domínio
 - Versionamento de banco com Flyway
 - Código organizado, extensível e pronto para produção
