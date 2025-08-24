# Task Manager (Spring Boot + Spring Security JWT)

A simple Task management REST API built with Spring Boot 3, secured end-to-end with Spring Security and JWT. It demonstrates clean layering with DTOs, validation, method-level authorization, Swagger/OpenAPI documentation, and MySQL persistence using Spring Data JPA.


## Key Features

- Spring Boot
  - Auto-configuration and opinionated starters (web, data-jpa, validation, security)
  - Embedded server (Tomcat) and simple application bootstrap
  - Configuration via application.properties
  - Static resource serving from src/main/resources/static
- RESTful API
  - Controllers exposing CRUD for tasks under /task
  - Request/response decoupled using DTOs
  - Validation annotations on incoming payloads (jakarta.validation)
- Persistence with Spring Data JPA (MySQL)
  - Entities: Task, UserAcc
  - Repositories: TaskRepo, UserRepo with derived query methods
  - Hibernate DDL auto-update and SQL logging
- Spring Security with JWT
  - Stateless authentication using JWT (Bearer tokens)
  - Custom JwtAuthenticationFilter integrated into the filter chain
  - CustomUserDetailsService backed by the DB for credential lookup
  - Password hashing with BCryptPasswordEncoder
  - Method-level security via @PreAuthorize for fine-grained role checks
  - Centralized HttpSecurity configuration with CSRF disabled for APIs, CORS enabled, and whitelisted endpoints
- Roles and Authorization
  - Roles stored as ROLE_USER / ROLE_ADMIN
  - Service methods protected: read for USER/ADMIN, write for ADMIN
- CORS Configuration
  - Global CORS with allowed origins/methods/headers (WebMvcConfigurer)
- API Documentation
  - springdoc-openapi UI at /swagger-ui.html (and /v3/api-docs/…)
  - OpenAPI metadata and JWT bearer security scheme configured
  - Controller/DTO annotations for rich docs (@Operation, @Schema, etc.)
- Error Handling
  - GlobalExceptionHandler with @ExceptionHandler methods for clean error responses
- Developer Aids
  - api.http client file with ready-to-run requests in IntelliJ/HTTP Client
  - Lombok for reduced boilerplate (@Data, @Builder, etc.)

## Architecture Overview

- Entry point: TaskManagerApplication
- Layers:
  - Controller: AuthController, TaskController
  - Service: TaskService (business logic + authorization via annotations)
  - Repository: TaskRepo, UserRepo
  - Model: Task, UserAcc
  - DTO: TaskDTO, AuthRequest, RegisterRequest
  - Config: Security, JwtAuthenticationFilter, JwtUtil, CustomUserDetailsService, WebConfig (CORS), Swagger (OpenAPI)
  - Exception: GlobalExceptionHandler

## Security Flow (JWT)

1. Register a user: POST /auth/register with username/password/role.
2. Login: POST /auth/login to obtain a JWT token.
3. Include token in Authorization header for protected endpoints:
   - Authorization: Bearer <token>
4. Token is parsed/validated by JwtAuthenticationFilter using JwtUtil.
5. Roles in DB (ROLE_USER / ROLE_ADMIN) drive access checks via @PreAuthorize.

Permit rules (SecurityFilterChain):
- Permit all: /auth/**, /v3/api-docs/**, /swagger-ui/**, /swagger-ui.html
- All others require authentication; method security further restricts by role.

## Endpoints Summary

- Auth
  - POST /auth/register — Create a user (role defaults to USER if not provided)
  - POST /auth/login — Authenticate and return a JWT
  - POST /auth/token-info — Inspect current token (requires Authorization)

- Tasks (require Authorization: Bearer <token>)
  - GET /task/get — List all tasks (USER/ADMIN)
  - GET /task/get/{id} — Get task by ID (USER/ADMIN)
  - POST /task/add — Add task (USER/ADMIN via @PreAuthorize; business write is allowed; change as needed)
  - PUT /task/update/{id} — Update task (ADMIN)
  - PUT /task/complete/{id} — Mark complete (ADMIN)
  - DELETE /task/delete/{id} — Delete task (ADMIN)
  - GET /task/get/name/{taskName} — Find tasks by name (USER/ADMIN)

Note: Check TaskService @PreAuthorize annotations for the exact role matrix.

## Data Model

- Task
  - id: Long (auto-generated)
  - taskName: String (not blank)
  - date: LocalDate (future or present)
  - comp: boolean (completion flag)

- UserAcc
  - id: Long (IDENTITY)
  - username: String (unique constraint suggested)
  - password: String (BCrypt-encoded)
  - role: String (e.g., ROLE_USER, ROLE_ADMIN)

## Configuration

application.properties (MySQL example):
- spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
- spring.datasource.username=root
- spring.datasource.password=********
- spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.show-sql=true
- spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


JWT
- Secret key currently defined in code (JwtUtil). For production, externalize to environment/config.

CORS
- Global allow-all in WebConfig; tighten for production.

## Running Locally

Prerequisites:
- Java 17+
- Maven 3.8+
- MySQL running and database taskdb created (or adjust URL/credentials)

Steps:
1. Create database:
   - CREATE DATABASE taskdb CHARACTER SET utf8mb4;
2. Configure src/main/resources/application.properties with your MySQL credentials.
3. Build and run:
   - mvn spring-boot:run
4. Open Swagger UI:
   - http://localhost:8080/swagger-ui.html

## Using the HTTP Client (api.http)

In IntelliJ IDEA, open src/main/java/com/api/task_manager/api.http and click the request gutter icons to:
- Register or login, capture token into {{auth_token}}
- Call protected Task APIs with the token automatically injected

## Notable Spring Concepts Used

- Core Spring Boot features: auto-config, starters, actuator-ready setup
- Spring MVC: controllers, request mapping, validation, exception handling
- Spring Data JPA: repositories, derived queries, entity mapping
- Spring Security:
  - Authentication: DaoAuthenticationProvider + CustomUserDetailsService
  - Authorization: HttpSecurity matchers and @PreAuthorize with roles
  - Stateless JWT auth with a custom OncePerRequestFilter
  - Password hashing via BCryptPasswordEncoder
- Bean configuration with @Configuration and @Bean
- OpenAPI/Swagger via springdoc-openapi
- CORS via WebMvcConfigurer
- Lombok to reduce boilerplate

## Hardening and Next Steps

- Move JWT secret and token settings to external configuration (e.g., env vars)
- Add refresh tokens and token revocation if needed
- Add unique constraint and indexes for users
- Improve GlobalExceptionHandler with @RestControllerAdvice
- Replace allow-all CORS with specific origins in production
- Add integration tests and testcontainers for DB

## License

This project is for educational/demo purposes. Add a LICENSE file if you plan to distribute.
