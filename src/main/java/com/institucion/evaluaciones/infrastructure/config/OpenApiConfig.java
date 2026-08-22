package com.institucion.evaluaciones.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * ============================================================================
 * CONFIGURACIÓN DE CONTRATOS OPENAPI 3 / SWAGGER
 * Aislada exclusivamente bajo el perfil 'dev' para garantizar la seguridad
 * ============================================================================
 */
@Configuration
@Profile("dev")
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Evaluaciones Académicas - API")
                        .description("Microservicio backend para el registro, publicación, impresión y ciclo de vida de instrumentos de evaluación académica. Implementado con Spring Boot 3, Spring Data JPA, PostgreSQL y Docker.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dirección de Docencia y Tecnologías")
                                .email("soporte.docencia@institucion.cl")
                                .url("https://institucion.cl"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Entorno Local de Desarrollo (Docker / Spring Boot)")
                ));
    }
}
