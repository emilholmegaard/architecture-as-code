# WebShop Architecture-as-Code

## Overview

This repository demonstrates Architecture-as-Code principles using ArchUnit tests and fitness functions for a web shop system with integrated case handling for returns and complaints.

## System Description

The system is a modern e-commerce platform that includes:

- **Product Catalog Management**: Browse and search products
- **Order Processing**: Shopping cart, checkout, and payment processing
- **Customer Management**: User profiles and order history
- **Case Handling System**: Manage returns, complaints, and customer service tickets
- **Notification System**: Email and SMS notifications for order updates

## Architecture

The system follows a **Hexagonal Architecture** (Ports and Adapters) pattern with clear separation of concerns:

### Layers

1. **Domain Layer** (`com.webshop.domain`)
   - Core business logic and entities
   - Domain services
   - Business rules enforcement

2. **Application Layer** (`com.webshop.application`)
   - Use cases orchestration
   - Input/Output ports definition
   - Transaction boundaries

3. **Infrastructure Layer** (`com.webshop.infrastructure`)
   - Database persistence
   - External service integration
   - Messaging and events

4. **Presentation Layer** (`com.webshop.presentation`)
   - REST API endpoints
   - DTOs and mappers
   - Request/Response handling

5. **Shared Layer** (`com.webshop.shared`)
   - Cross-cutting concerns
   - Utilities and helpers
   - Common exceptions

6. **Config Layer** (`com.webshop.config`)
   - Application configuration
   - Security settings
   - Database configuration

## Architecture Diagram

See `src/main/resources/architecture-diagram.puml` for the PlantUML system architecture diagram.

## Running Architecture Tests

### Prerequisites

- Java 23+
- Maven 3.8+
- Docker (optional)

### Execute Tests

```bash
# Run all tests
mvn test

# Run only architecture tests
mvn test -Dtest=*ArchitectureTest

# Run only fitness functions
mvn test -Dtest=*FitnessFunction
```

## Architecture Rules

### Layer Dependencies

- Domain layer has no dependencies on other layers
- Application layer depends only on Domain
- Infrastructure depends on Application and Domain
- Presentation depends on Application and Domain
- Config can be accessed by Infrastructure and Presentation

### Package Conventions

- Controllers must be in `presentation.rest` package
- Entities must be in `domain.model` package
- Repositories must be in `infrastructure.persistence` package
- Use cases must be in `application.usecase` package

### Naming Conventions

- Controllers must end with "Controller"
- Services must end with "Service"
- Repositories must end with "Repository"
- Use cases must end with "UseCase"
- DTOs must end with "DTO"

## Fitness Functions

### 1. Modularity Fitness Function

Measures the modularity of the system based on package cohesion and coupling.

### 2. Coupling Fitness Function

Monitors the coupling between packages and ensures it stays within acceptable thresholds.

### 3. Cohesion Fitness Function

Evaluates the cohesion within packages to ensure related functionality stays together.

### 4. Complexity Fitness Function

Tracks cyclomatic complexity and ensures methods don't exceed complexity thresholds.

### 5. Test Coverage Fitness Function

Ensures minimum test coverage for critical components.

## Docker Support

The application can be containerized using Docker. The provided Dockerfile uses a multi-stage build process to create an optimized container image.

### Building the Docker Image

```bash
docker build -t webshop .
```

### Running the Container

```bash
docker run -p 8080:8080 webshop
```

The application will be accessible at `http://localhost:8080`.

### Docker Configuration

The Docker setup includes:

- Multi-stage build for optimal image size
- OpenJDK 23 (Temurin) as the base image
- Exposed port 8080 for the web application
- Automatic inclusion of the compiled JAR file

## Metrics Dashboard

Run the following command to generate an architecture metrics report:

```bash
mvn test -Dtest=*FitnessFunction -DgenerateReport=true
```

## Contributing

1. Ensure all architecture tests pass before committing
2. Update fitness functions when adding new architectural constraints
3. Document any architectural decisions in `architecture-decisions.md`

## License

MIT License
