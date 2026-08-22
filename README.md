# 🎓 Sistema de Gestión de Evaluaciones Académicas - Microservicio Backend

[![Java](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16--alpine-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![OpenAPI](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5%20%7C%20Mockito-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Postman](https://img.shields.io/badge/Postman-Collection%20Ready-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)

Microservicio backend institucional diseñado para la administración, control de inventario de copias, publicación y ciclo de impresión de instrumentos de evaluación académica. Construido con **Java 21**, **Spring Boot 3.3.5**, **Spring Data JPA**, **PostgreSQL** y **Docker**, aplicando rigurosamente los principios de **Arquitectura Limpia (Clean Architecture)** y **Diseño Guiado por el Dominio (DDD)**.

---

## 🏛️ Arquitectura Limpia & Principios DDD

El proyecto organiza sus responsabilidades en capas estrictamente desacopladas para garantizar alta cohesión y bajo acoplamiento:

```
gestion-evaluaciones-backend/
├── docker-compose.yml                      # Orquestación del motor PostgreSQL 16 Alpine
├── pom.xml                                 # Dependencias Maven y configuración de plugins
├── postman_collection.json                 # Colección de pruebas de integración para Postman/Bruno
├── src/
│   ├── main/
│   │   ├── java/com/institucion/evaluaciones/
│   │   │   ├── GestionEvaluacionesApplication.java   # Entry Point Spring Boot
│   │   │   ├── application/                          # Capa de Aplicación (Casos de Uso)
│   │   │   │   ├── EvaluationService.java            # Orquestador del ciclo de vida y reglas de negocio
│   │   │   │   ├── EvaluationStatusPrinterService.java # Servicio de impresión y alertas
│   │   │   │   └── NotificationService.java          # Puerto de notificaciones
│   │   │   ├── domain/                               # Capa de Dominio (Puro / Sin Frameworks)
│   │   │   │   ├── model/                            # Entidad Aggregate Root (Evaluation)
│   │   │   │   │   └── valueobjects/                 # Value Objects (EvaluationScore)
│   │   │   │   ├── repository/                       # Puerto de Repositorio (EvaluationRepository)
│   │   │   │   └── exception/                        # Excepciones de Dominio Específicas
│   │   │   │       ├── EvaluationNotFoundException.java
│   │   │   │       ├── EvaluationNotPublishedException.java
│   │   │   │       ├── InvalidCopyQuantityException.java
│   │   │   │       └── InvalidEvaluationDateException.java
│   │   │   └── infrastructure/                       # Capa de Infraestructura (Adaptadores)
│   │   │       ├── config/                           # Configuración (OpenAPI, CORS, DataInitializer)
│   │   │       │   ├── OpenApiConfig.java            # Contratos Swagger (perfil 'dev')
│   │   │       │   └── DataInitializer.java         # Semillero de datos iniciales en PostgreSQL
│   │   │       ├── notification/                     # Implementación de alertas institucionales
│   │   │       ├── persistence/                      # Adaptadores JPA y Repositorios
│   │   │       │   ├── entity/                       # EvaluationJpaEntity (Mapeo ORM tbl_evaluations)
│   │   │       │   ├── repository/                   # SpringDataEvaluationRepository
│   │   │       │   ├── PostgresEvaluationRepositoryAdapter.java # Adaptador primario JPA
│   │   │       │   └── InMemoryEvaluationRepository.java        # Repositorio en memoria
│   │   │       └── rest/                             # Controladores REST, DTOs y Advice
│   │   │           ├── EvaluationController.java     # Endpoints CRUD y acciones de estado
│   │   │           ├── advice/                       # GlobalExceptionHandler (@RestControllerAdvice)
│   │   │           └── dto/                          # Records DTO tipados
│   │   └── resources/
│   │       ├── application.yml                       # Configuración general y perfiles activos
│   │       ├── application-dev.yml                   # Perfil DEV (PostgreSQL + Swagger activo)
│   │       └── application-prod.yml                  # Perfil PROD (Swagger estrictamente bloqueado)
│   └── test/                                         # Suite de Pruebas Automatizadas (39 tests unitarios e integrados)
```

---

## 📋 Reglas de Negocio Institucionales

1. **Rango de Copias Solicitadas:** El número de copias para una evaluación debe ser estrictamente **mayor a 0 y menor a 50** (`1 <= copias <= 49`).
2. **Fecha de Examen Válida:** No se permite registrar evaluaciones con fechas en el pasado respecto al día actual.
3. **Reglas de Impresión de Evaluaciones (`/api/v1/evaluations/{id}/print`):**
   - **Regla 1 (Estado Requerido):** La evaluación debe encontrarse en estado **`Publicada`**.
   - **Regla 2 (Límite de Copias):** La cantidad de copias debe cumplir con el rango permitido (1-49).
   - **Regla 3 (Anticipación Estricta):** La impresión solo puede ejecutarse **exactamente el día anterior** a la fecha fijada para el examen. Si la fecha del examen es hoy o futura pero anterior al día previo, la orden es rechazada con `InvalidEvaluationDateException`.

---

## 📡 Catálogo de Endpoints REST

| Verbo | Ruta | Descripción | Códigos HTTP |
|---|---|---|---|
| `GET` | `/api/v1/evaluations` | Listar catálogo completo de evaluaciones | `200 OK` |
| `GET` | `/api/v1/evaluations/{id}` | Obtener detalle de una evaluación por ID | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/evaluations` | Registrar nueva evaluación académica | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/v1/evaluations/{id}` | Actualizar datos de una evaluación | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/v1/evaluations/{id}` | Eliminar evaluación físicamente | `204 No Content` / `404 Not Found` |
| `POST` | `/api/v1/evaluations/{id}/publish` | Publicar evaluación para el centro de copiado | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/evaluations/{id}/print` | Procesar orden de impresión según reglas de negocio | `200 OK` / `422 Unprocessable` / `400 Bad Request` |

---

## 🛡️ Manejo Centralizado de Excepciones (`@RestControllerAdvice`)

Todas las excepciones de dominio son interceptadas por `GlobalExceptionHandler`, devolviendo un formato JSON estándar:

```json
{
  "timestamp": "2026-08-22T17:15:00",
  "status": 400,
  "error": "Bad Request - Invalid Date",
  "message": "Aún no corresponde imprimir. La impresión solo está autorizada exactamente el día anterior a la evaluación (Fecha autorizada: 2026-08-24).",
  "path": "/api/v1/evaluations/1/print"
}
```

---

## 🚀 Puesta en Marcha Local

### 1. Requisitos Previos
- **Java JDK:** 21 LTS
- **Apache Maven:** 3.8+
- **Docker & Docker Compose**

### 2. Iniciar PostgreSQL con Docker Compose
```bash
docker compose up -d
```
> Levanta el contenedor `gestion-evaluaciones-postgres-db` en el puerto `5432` con volumen local persistente.

### 3. Ejecutar Pruebas Automatizadas
```bash
mvn clean test
```
> Ejecuta los 39 tests unitarios e integrados con cobertura completa generada vía `jacoco-maven-plugin`.

### 4. Iniciar el Microservicio
```bash
mvn spring-boot:run
```

### 5. Documentación Interactiva Swagger-UI
Abre en tu navegador:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**  
👉 **Especificación JSON OpenAPI 3:** `http://localhost:8080/v3/api-docs`

---

## 📮 Pruebas de Integración con Postman / Bruno

El repositorio incluye el archivo [`postman_collection.json`](./postman_collection.json) con todas las peticiones preconfiguradas:
1. Abre **Postman** o **Bruno**.
2. Selecciona **Import** y carga `postman_collection.json`.
3. Ejecuta las peticiones contra `http://localhost:8080/api/v1/evaluations`.

---

## 👨‍💻 Autor

- **Claudio Durán** - [@claudux](https://github.com/claudux)
