# Spring Boot Rest Service sample

## Table of Contents

- [Project Setup](#project-setup)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)

---

## Project Setup

### Prerequisites

- **Java 17**: Ensure that you have JDK 21 installed.
- **Maven**: The project uses Maven as the build tool.
-
### Running the Application

1. Clone the repository:

 ```bash
 git clone https://github.com/sampleboot/userapi
 ```

2. Navigate to the project directory:

 ```bash
 cd userapi
 ```

3. Install the dependencies and build the project:

 ```bash
 mvn clean install
 ```

4. Start the application by running:

 ```bash
 mvn spring-boot:run
 ```

5. The application will start on a default port (e.g., `http://localhost:8080`), and you can interact with the API using Postman, Insomnia, or cURL.

---

### Running Tests

To run the unit and integration tests, execute the following command:

```bash
mvn test
```
---

## Project Structure

```
src
├── main
│ ├── java
│ │ └── com.example.userapi
│ │ ├── controller # REST Controller for handling HTTP requests
│ │ ├── entity # JPA entities (e.g., UserModel)
│ │ ├── repository # Data repositories for database operations
│ │ └── service # Business logic layer
│ └── resources
│ └── application.properties # Database configuration
├── test
│ └── java
│ └── com.example.userapi
│ └── controller # Unit and Integration tests using RestAssured
| └── service # Unit tests using RestAssured
```

---

## Technologies Used

- **Spring Boot 3.5.0**: For building the backend REST API.
- **Spring Data JPA**: For database interaction and ORM.
- **H2 Database**: Inmemory database to demonstrate the functionality
- **RestAssured**: For testing REST endpoints in the integration tests.
- **Maven**: For building the project and managing dependencies.
- **Swagger**: For the documentation of the project.


---

## Documentation

Access the API documentation on http://localhost:8080/swagger-ui.html
