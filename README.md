# Biblioteca API

API REST para gerenciamento completo de uma biblioteca, incluindo autores, livros, membros, empréstimos e multas.

## Sobre o Projeto

Este sistema gerencia:
- Cadastro de livros e autores
- Registro de membros da biblioteca
- Controle de empréstimos e devoluções
- Gestão de multas por atraso

## Tecnologias

- Java 21
- Spring Boot 3.4.5
- MySQL
- Swagger/OpenAPI

## Executando o Projeto

### Requisitos
- JDK 21
- Maven
- MySQL


Ajuste as configurações de conexão em `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/Biblioteca?useSSL=false&serverTimezone=UTC
    username: seu_usuario
    password: sua_senha
```

### Rodando a Aplicação

```bash
# Compilar e executar
mvn spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080/api

## Acessando o Swagger

Para explorar e testar a API:

1. Inicie a aplicação
2. Acesse: http://localhost:8080/api/swagger-ui/index.html
3. A interface exibirá todos os endpoints organizados por categorias

Através do Swagger você pode:
- Ver todos os endpoints disponíveis
- Testar operações usando o botão "Try it out"
- Visualizar modelos de dados e parâmetros
- Executar requisições e ver as respostas em tempo real