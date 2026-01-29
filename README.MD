📘 User & Card API
📌 Visão Geral

Esta API foi desenvolvida como parte de um teste técnico, seguindo boas práticas de arquitetura backend, segurança, separação de responsabilidades e padrão REST.

O sistema gerencia:

Usuários

Cartões

Autenticação com JWT

Controle de acesso por roles (USER / ADMIN)

A aplicação foi construída com Spring Boot, utilizando MySQL, JPA/Hibernate e Spring Security.

🧱 Arquitetura

A aplicação segue uma arquitetura em camadas:

Controller → Service → Repository → Database

📂 Estrutura de Pacotes

com.desafio.userapi
├── config # Configurações de segurança e Swagger
├── controller # Controllers REST (Auth, User, Card, Admin)
├── dto # DTOs de entrada e saída
├── entity # Entidades JPA
├── exception # Exceptions customizadas e handler global
├── repository # Repositórios JPA
├── security # JWT, filtros e autenticação
└── service # Regras de negócio

🔐 Autenticação & Segurança

Autenticação baseada em JWT

Sessão stateless

Filtro JWT (OncePerRequestFilter)

Controle de acesso por roles:

USER

ADMIN

📌 Regras de acesso

Usuários USER só podem acessar e alterar seus próprios cartões

Usuários ADMIN podem:

visualizar todos os usuários

visualizar todos os cartões

gerenciar cartões de qualquer usuário

📄 Endpoints Principais
🔑 Autenticação
Método	Endpoint	Descrição
POST	/auth/register	Cadastro de usuário
POST	/auth/login	Login e geração de token JWT
👤 Usuário (USER)
Método	Endpoint	Descrição
GET	/users/me	Dados do usuário autenticado
💳 Cartões (USER)
Método	Endpoint	Descrição
POST	/cards	Cadastrar cartão
GET	/cards	Listar cartões do usuário
PATCH	/cards/{id}/toggle	Ativar/Desativar cartão
DELETE	/cards/{id}	Remover cartão

Validação de segurança:
O usuário não consegue alterar cartões que não sejam de sua posse.

🛠️ Administração (ADMIN)
Método	Endpoint	Descrição
GET	/admin/cards	Listar todos os cartões
GET	/admin/cards/user/{userId}	Cartões de um usuário
PATCH	/admin/cards/{id}/toggle	Ativar/Desativar qualquer cartão
DELETE	/admin/cards/{id}	Remover cartão
📘 Swagger

A documentação da API está disponível em:

http://localhost:8080/swagger-ui.html

Possui botão Authorize

Suporte a JWT no header Authorization

⚙️ Configuração
application.yml (exemplo)

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userapi
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: minha-chave-secreta-super-segura

▶️ Como Executar
Localmente

./mvnw spring-boot:run

A aplicação estará disponível em:
http://localhost:8080

🧪 Tratamento de Erros

A API possui tratamento global de exceções, retornando respostas padronizadas:

400 → Erros de negócio

403 → Acesso negado

404 → Recurso não encontrado

500 → Erro interno

🧩 Padrões e Boas Práticas

DTOs para entrada e saída

Exceptions customizadas

Controllers enxutos

Regras de negócio no Service

Segurança no domínio (não confiar no client)

Código organizado e extensível
