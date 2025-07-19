# Project Architecture Overview

This document provides an overview of the `smart-finance` project's architecture, highlighting key components, design patterns, and technology choices.

## 1. Layered Architecture

The project follows a traditional **Layered Architecture** (often referred to as N-tier architecture), which promotes separation of concerns, maintainability, and scalability.

*   **Presentation Layer (Controllers)**: Handles HTTP requests, routes them to the appropriate service, and returns HTTP responses.
    *   **Components**: Classes in `Finance.organizador_financeiro.controller`.
    *   **Responsibilities**: Request/response handling, input validation (basic), and delegating business logic to the Service Layer.
    *   **Key Principle**: Thin controllers. They should not contain business logic.

*   **Service Layer (Business Logic)**: Contains the core business rules and logic of the application. It orchestrates operations, performs complex validations, and interacts with the Data Access Layer.
    *   **Components**: Classes in `Finance.organizador_financeiro.service`.
    *   **Responsibilities**: Implementing business rules, transaction management, and coordinating data operations.
    *   **Key Principle**: Encapsulates business logic, independent of the presentation or data access details.

*   **Data Access Layer (Repositories)**: Provides an abstraction over the persistence mechanism. It handles database operations (CRUD - Create, Read, Update, Delete) and maps objects to database records.
    *   **Components**: Interfaces in `Finance.organizador_financeiro.repository` (using Spring Data JPA).
    *   **Responsibilities**: Data persistence, querying, and mapping entities to database tables.
    *   **Key Principle**: Decouples business logic from database specifics.

*   **Domain Layer (Entities)**: Represents the core business objects and their relationships. These are plain Java objects (POJOs) that map directly to database tables.
    *   **Components**: Classes in `Finance.organizador_financeiro.domain`.
    *   **Responsibilities**: Defining the structure and relationships of business data.
    *   **Key Principle**: Contains the "truth" about the business data.

## 2. Data Transfer Objects (DTOs)

**DTOs** are used to transfer data between different layers of the application, especially between the Presentation Layer (Controllers) and the Service Layer, and often to/from external clients (e.g., frontend applications).

*   **Components**: Classes in `Finance.organizador_financeiro.dto`.
*   **Purpose**:
    *   **Data Hiding**: Prevents exposing internal entity structures directly to the client, enhancing security and flexibility for future changes.
    *   **Reduced Data Transfer**: Allows sending only the necessary data, optimizing network payload.
    *   **Decoupling**: Changes in the internal entity structure do not necessarily impact the API contract (DTOs).
*   **Implementation Choice**:
    *   **Lombok**: Used to reduce boilerplate code (getters, setters, constructors) in DTOs, making them more concise and readable. This is a common and recommended practice in modern Java projects.
    *   **Why not Java Records/Interfaces for DTOs?**: While Java Records (Java 16+) offer extreme conciseness and immutability, and interfaces can be used as projections in specific Spring Data JPA contexts, the current project leverages Lombok for DTOs to maintain consistency with existing patterns and ensure broader compatibility with Spring's default deserialization mechanisms and MapStruct. For new projects on Java 16+, Records would be a strong recommendation.

### DTOs for Reporting (e.g., `CategorySpendingDTO`)

It's important to distinguish between DTOs that represent core entities (like `TransactionDTO`, `UserDTO`, `CategoryDTO`) and DTOs that represent **aggregated or calculated data for reports**.

*   **Example: `CategorySpendingDTO`**
    *   This DTO (`CategorySpendingDTO`) is designed to carry information about spending per category (e.g., `categoryName`, `totalSpent`).
    *   **Key Distinction**: `CategorySpendingDTO` is **not** a direct representation of a database entity that you would perform CRUD operations on. You don't "create" a `CategorySpendingDTO` directly, nor do you "update" or "delete" it like a `Transaction` or `Category`.
    *   **Purpose**: It's the *result* of a calculation or aggregation performed by the `ReportService` based on existing `Transaction` and `Category` data. Its sole purpose is to present summarized financial information to the user.
    *   **Controller Responsibility**: Therefore, there is no dedicated `CategorySpendingController`. The endpoint for retrieving this data (`/api/reports/spending-by-category`) resides within the `ReportController`, which is responsible for exposing all reporting functionalities. This adheres to the principle of **separation of concerns**:
        *   **Entity Controllers (e.g., `TransactionController`, `CategoryController`)**: Manage the lifecycle (CRUD) of core domain entities.
        *   **Report Controllers (e.g., `ReportController`)**: Provide access to aggregated and calculated data derived from those entities.

## 3. Mappers (MapStruct)

**Mappers** are responsible for converting data between different object types, primarily between **Entities** (Domain Layer) and **DTOs**.

*   **Components**: Interfaces in `Finance.organizador_financeiro.mapper`.
*   **Technology Choice**: **MapStruct**.
*   **Purpose**:
    *   **Automated Mapping**: MapStruct is an annotation processor that generates boilerplate mapping code automatically during compilation. This eliminates manual mapping code, which is error-prone and time-consuming.
    *   **Performance**: Generates plain Java code, avoiding runtime reflection, leading to better performance compared to other mapping libraries.
    *   **Type Safety**: Provides compile-time type checking, catching mapping errors early.
    *   **Conciseness**: Reduces code verbosity significantly.
*   **Key Principle**: Mappers should be stateless and focused solely on data transformation. They should not contain business logic. Complex logic (e.g., fetching related entities for mapping) should reside in the Service Layer.

## 4. Security

The project integrates Spring Security for authentication and authorization.

*   **Components**: Classes in `Finance.organizador_financeiro.security`.
*   **Responsibilities**: User authentication (login), token generation (JWT), and securing API endpoints.
*   **Key Principle**: Ensures that only authenticated and authorized users can access specific resources.

## 5. Database

*   **Technology**: H2 Database (in-memory for development/testing).
*   **Persistence Framework**: Spring Data JPA.
*   **Key Principle**: Simplifies data access operations by providing repository interfaces.

## 6. Development Workflow (Git Branching Strategy)

The project follows a branching strategy to manage development effectively:

*   **`main` branch**: Represents the production-ready, stable version of the application. Only fully tested and approved code is merged here.
*   **`dev` branch**: The main development branch. All new features and bug fixes are eventually merged into `dev` after being reviewed and tested.
*   **`feature/*` branches**: Created from `dev` for each new feature or significant task. This isolates development work, preventing interference with other ongoing tasks. Once a feature is complete, it's merged back into `dev`.
*   **`refactor/*` branches**: Similar to feature branches, used for significant refactoring efforts to isolate changes and ensure stability.

This strategy promotes:
*   **Collaboration**: Multiple developers can work on different features simultaneously.
*   **Stability**: The `main` branch remains stable.
*   **Code Review**: Facilitates code reviews before merging into `dev`.
*   **Traceability**: Clear history of changes for each feature.
