# Url Shortener

## 1. Application summary

Url Shortener is a web application built with Spring Boot that allows users to create shortened URLs. It provides a simple interface for generating and managing shortened links.

## 2. Setup/Installation

### Prerequisites

- Docker engine

### Installation Steps

1. Clone the repository:
   ```sh
   git clone git@github.com:jakubpraszkowski/UrlShortener.git
   cd url-shortener

2. Create a .env file in the root directory with the following content:
    ```shell
   DATABASE_URL=jdbc:postgresql://java_db:5432/prod_db
    DATABASE_USERNAME=postgres
    DATABASE_PASSWORD=postgres
    SPRING_PROFILES_ACTIVE=online
   
3. Build and start the project:
    ```sh
    docker-compose up --build

## 3. How to run tests
To run the tests, use the following command:

```sh

mvn test
```

## 4. Running API documentation
The project uses Springdoc OpenAPI to generate API documentation. To access the API documentation, follow these steps:
1. Ensure the application is running.
2. Open your browser and navigate to http://localhost:8080/swagger.

## 5. Application properties
The application properties are configured in the following files:
- application.properties: Common properties.
- application-dev.properties: Development-specific properties.
- application-online.properties: Online-specific properties.

## 5. System architecture

The application follows a typical Spring Boot architecture with the following components:  
- Controller: Handles HTTP requests and responses.
- Service: Contains business logic.
- Repository: Interacts with the database.
- Entity: Represents database tables.

## 6. Technology
- Java 17
- Docker
- PostgreSQL
- Maven
- JUnit 5
- Mockito 2
- Swagger