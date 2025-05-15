package io.acordi.msbiblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API de Gerenciamento de Biblioteca")
                        .description("API REST para gerenciamento completo de uma biblioteca, incluindo autores, livros, membros, empréstimos e multas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Administrador da Biblioteca")
                                .email("admin@biblioteca.com")
                                .url("https://www.biblioteca.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}

