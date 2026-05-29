# Products Microservice

![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?logo=springboot)
![Springdoc OpenAPI](https://img.shields.io/badge/Springdoc%20OpenAPI-3.0.2-orange)
![H2](https://img.shields.io/badge/H2-en%20memoria-lightgrey)
![JaCoCo](https://img.shields.io/badge/Cobertura%20mínima-80%25-success)
![Docker](https://img.shields.io/badge/Docker-Hub-blue?logo=docker)

Microservicio RESTful de gestión de productos desarrollado con **Spring Boot 4** y **Java 25**. Expone una API CRUD completa documentada con **OpenAPI 3 / Swagger UI**, siguiendo una arquitectura presentation-domain-data alineada a las prácticas actuales de microservicios.

---

## Contenido

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Ejecución local](#ejecución-local)
- [Documentación de la API](#documentación-de-la-api)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Cobertura de código](#cobertura-de-código)
- [Configuración](#configuración)
- [CI/CD](#cicd)

---

## Tecnologías

| Tecnología          | Versión | Rol                              |
|---------------------|---------|----------------------------------|
| Java                | 25      | Lenguaje                         |
| Spring Boot         | 4.0.6   | Framework principal              |
| Spring Web MVC      | —       | Capa REST                        |
| Spring Data JPA     | —       | Persistencia                     |
| H2                  | 2.4     | Base de datos en memoria         |
| Spring Actuator     | —       | Observabilidad y health checks   |
| Springdoc OpenAPI   | 3.0.2   | Documentación / Swagger UI       |
| Bean Validation     | —       | Validación de entrada            |
| Lombok              | —       | Reducción de boilerplate         |
| JaCoCo              | 0.8.14  | Cobertura de código              |

---

## Arquitectura

El microservicio sigue una **arquitectura de tres capas** con separación explícita de responsabilidades:

```
presentation   ←  Controladores REST, DTOs de entrada (request) y salida (response)
domain         ←  Entidades JPA, lógica de negocio (servicio), mappers
data           ←  Repositorios Spring Data JPA
```

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/dev/edsonmm/products/
│   │   ├── config/
│   │   │   └── OpenApiConfig.java                    # Configuración de Swagger / OpenAPI
│   │   ├── data/
│   │   │   └── repository/
│   │   │       └── ProductRepository.java            # Repositorio JPA
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Product.java                      # Entidad JPA
│   │   │   ├── mapper/
│   │   │   │   └── ProductMapper.java                # Conversión request → entity → response
│   │   │   └── service/
│   │   │       ├── interfaces/
│   │   │       │   └── ProductService.java           # Contrato del servicio
│   │   │       └── implement/
│   │   │           └── ProductServiceImpl.java       # Implementación
│   │   ├── exception/
│   │   │   ├── response/
│   │   │   │   ├── ErrorResponse.java                # Respuesta de error genérica
│   │   │   │   └── ValidationErrorResponse.java      # Respuesta de error de validación
│   │   │   ├── ErrorHandler.java                     # Manejador global de excepciones
│   │   │   └── ProductNotFoundException.java         # Excepción de dominio
│   │   ├── presentation/
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java            # Endpoints REST
│   │   │   ├── request/
│   │   │   │   └── ProductRequest.java               # DTO de entrada con validaciones
│   │   │   └── response/
│   │   │       └── ProductResponse.java              # DTO de salida (record inmutable)
│   │   └── ProductsApplication.java                  # Punto de entrada
│   └── resources/
│       ├── application.yaml                          # Configuración de la aplicación
│       └── data.sql                                  # Datos iniciales de desarrollo
└── test/
    └── java/dev/edsonmm/products/
        ├── domain/
        │   ├── mapper/     ProductMapperTest.java
        │   └── service/    ProductServiceImplTest.java
        ├── presentation/
        │   ├── controller/ ProductControllerTest.java
        │   └── request/    ProductRequestValidationTest.java
        └── ProductsApplicationTests.java
```

---

## Requisitos previos

- **Java 25**
- **Maven 3.9+** (o usar el wrapper `./mvnw` incluido en el proyecto)

---

## Ejecución local

```bash
# Compilar y empaquetar
./mvnw clean package

# Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación inicia en `http://localhost:8080`.

Al arrancar, el esquema se crea automáticamente y se cargan cinco productos de prueba desde `data.sql`.

---

## Documentación de la API

Una vez iniciada la aplicación, la documentación interactiva está disponible en:

| Recurso         | URL                                      |
|-----------------|------------------------------------------|
| Swagger UI      | http://localhost:8080/swagger-ui.html    |
| OpenAPI JSON    | http://localhost:8080/api-docs           |
| Health check    | http://localhost:8080/actuator/health    |

Swagger UI permite explorar y ejecutar todos los endpoints directamente desde el navegador, con los esquemas de request y response completamente documentados, incluyendo los modelos de error.

---

## Endpoints

| Método     | Ruta                    | Descripción                     | Códigos HTTP  |
|------------|-------------------------|---------------------------------|---------------|
| `GET`      | `/api/products`         | Lista todos los productos       | 200           |
| `GET`      | `/api/products/{id}`    | Obtiene un producto por ID      | 200, 404      |
| `POST`     | `/api/products`         | Crea un nuevo producto          | 201, 400      |
| `PUT`      | `/api/products/{id}`    | Actualiza un producto existente | 200, 400, 404 |
| `DELETE`   | `/api/products/{id}`    | Elimina un producto             | 204, 404      |

### Validaciones del cuerpo de la petición

| Campo         | Restricciones                            |
|---------------|------------------------------------------|
| `name`        | Obligatorio · 2–100 caracteres           |
| `description` | Opcional · máximo 500 caracteres         |
| `price`       | Obligatorio · valor ≥ 0                  |
| `stock`       | Obligatorio · valor ≥ 0                  |
| `active`      | Opcional · booleano (por defecto `true`) |

Los errores de validación devuelven un `400 Bad Request` con un mapa detallado de campo → mensaje de error.

---

## Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar un test específico
./mvnw test -Dtest=NombreDelTest

# Ejecutar un método de test específico
./mvnw test -Dtest=NombreDelTest#nombreDelMetodo
```

| Suite de tests                  | Tipo                          | Tests |
|---------------------------------|-------------------------------|------:|
| `ProductsApplicationTests`      | Carga de contexto Spring      |     1 |
| `ProductMapperTest`             | Unitario                      |     7 |
| `ProductServiceImplTest`        | Integración con H2            |    10 |
| `ProductControllerTest`         | Capa web (`@WebMvcTest`)      |    13 |
| `ProductRequestValidationTest`  | Unitario (Bean Validation)    |    12 |
| **Total**                       |                               |**43** |

---

## Cobertura de código

JaCoCo está configurado con umbrales mínimos de cobertura que se validan en la fase `verify`. El build falla si no se alcanzan los mínimos.

```bash
./mvnw verify
```

| Métrica  | Umbral mínimo |
|----------|---------------|
| Líneas   | 80 %          |
| Ramas    | 60 %          |

El reporte en formato XML se genera en `target/site/jacoco/jacoco.xml`, compatible con SonarQube.

---

## Configuración

Todas las propiedades se encuentran en `src/main/resources/application.yaml`.

| Propiedad                                        | Valor por defecto   | Descripción                                                      |
|--------------------------------------------------|---------------------|------------------------------------------------------------------|
| `server.port`                                    | `8080`              | Puerto del servidor                                              |
| `spring.application.name`                        | `products-service`  | Nombre del servicio                                              |
| `spring.jpa.hibernate.ddl-auto`                  | `create-drop`       | Gestión del esquema (recrear en cada arranque)                   |
| `spring.jpa.defer-datasource-initialization`     | `true`              | Ejecuta `data.sql` después de que Hibernate crea el esquema      |
| `spring.jpa.open-in-view`                        | `false`             | Deshabilita el antipatrón Open Session in View                   |
| `springdoc.api-docs.path`                        | `/api-docs`         | Ruta del JSON de OpenAPI                                         |
| `springdoc.swagger-ui.path`                      | `/swagger-ui.html`  | Ruta de la interfaz Swagger UI                                   |
| `management.endpoints.web.exposure.include`      | `health, info`      | Endpoints de Actuator expuestos                                  |
| `management.endpoint.health.show-details`        | `always`            | Detalle completo en el health check                              |

---

## CI/CD

El pipeline está implementado con **Jenkins declarativo** + **Docker agents**. Cada stage corre en su propio contenedor aislado; el workspace del host es compartido entre stages Maven mediante `skipDefaultCheckout()`.

### Pipeline — stages

```
Build → Test → Coverage → Package → Sonar → Docker
```

| Stage      | Agent Docker                        | Comando Maven                    | Propósito                                      |
|------------|-------------------------------------|----------------------------------|------------------------------------------------|
| Build      | `maven:3.9-eclipse-temurin-25`      | `mvn clean compile`              | Falla rápido ante errores de compilación       |
| Test       | `maven:3.9-eclipse-temurin-25`      | `mvn test`                       | Ejecuta los 43 tests y publica resultados JUnit|
| Coverage   | `maven:3.9-eclipse-temurin-25`      | `mvn jacoco:report`              | Genera el reporte XML de cobertura             |
| Package    | `maven:3.9-eclipse-temurin-25`      | `mvn package -DskipTests`        | Produce el FAT JAR y lo archiva en Jenkins     |
| Sonar      | `maven:3.9-eclipse-temurin-25`      | `mvn sonar:sonar`                | Análisis de calidad y cobertura en SonarQube   |
| Docker     | `docker:29.4.0-cli`                 | `docker build` + `docker push`   | Publica la imagen en DockerHub                 |

El stage Docker usa **DooD** (Docker outside of Docker) montando el socket del host (`/var/run/docker.sock`), sin correr como root.

La imagen publicada en DockerHub es:

```
edsonmgoz/products-microservice:{version}
edsonmgoz/products-microservice:latest
```

### Credenciales requeridas en Jenkins

Antes de ejecutar el pipeline, deben existir las siguientes credenciales en **Jenkins → Manage Jenkins → Credentials**.

#### 1. `dockerhub-credentials`

Permite al stage Docker autenticarse en DockerHub para hacer `docker push`.

| Campo      | Valor                              |
|------------|------------------------------------|
| Tipo       | **Username with password**         |
| ID         | `dockerhub-credentials`            |
| Username   | `edsonmgoz`                        |
| Password   | Token de acceso de DockerHub       |

Para generar el token en DockerHub: **Account Settings → Personal access tokens → Generate new token** (permisos: `Read & Write`).

#### 2. `sonar-token`

Permite al stage Sonar autenticarse en el servidor SonarQube. El `sonar-maven-plugin 4.x` lo lee automáticamente desde la variable de entorno `SONAR_TOKEN`.

| Campo      | Valor                              |
|------------|------------------------------------|
| Tipo       | **Secret text**                    |
| ID         | `sonar-token`                      |
| Secret     | Token de usuario de SonarQube      |

Para generar el token en SonarQube: **Mi cuenta → Security → Generate token** (tipo: `User Token`).

> El servidor SonarQube también debe estar configurado en **Jenkins → Manage Jenkins → System → SonarQube servers** con el nombre `sonarqube` y la URL del servidor.
