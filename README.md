# Banking API

A RESTful banking API built with Java and Spring Boot that provides basic account management and banking operations.

The project was developed as a backend portfolio project, with a focus on clean architecture, business logic, transaction management, persistence, and unit testing.

## Features

- Create and retrieve bank accounts
- Deposit money into an account
- Withdraw money with balance validation
- Transfer money between accounts
- Prevent transfers between the same account
- Record deposits, withdrawals, and transfers
- Retrieve transaction history
- Retrieve transactions for a specific account
- Handle invalid operations with appropriate HTTP responses

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Git

## Architecture

The application follows a layered architecture:

```text
Client
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
PostgreSQL
```

- **Controller** — Handles HTTP requests and responses.
- **Service** — Contains the business logic and validation rules.
- **Repository** — Provides database access through Spring Data JPA.
- **Model** — Represents the application entities.

## API Endpoints

### Accounts

| Method | Endpoint | Description |
|---|---|---|
| POST | `/accounts` | Create an account |
| GET | `/accounts` | Retrieve all accounts |
| GET | `/accounts/{id}` | Retrieve an account by ID |
| POST | `/accounts/{id}/deposit` | Deposit money |
| POST | `/accounts/{id}/withdraw` | Withdraw money |
| POST | `/accounts/{id}/transfer` | Transfer money to another account |

### Transactions

| Method | Endpoint | Description |
|---|---|---|
| GET | `/transactions` | Retrieve all transactions |
| GET | `/transactions/account/{accountId}` | Retrieve transactions for an account |

## Example

Create an account:

```json
{
  "ownerName": "John Doe",
  "email": "john@example.com",
  "balance": 1000.00
}
```

Transfer money:

```json
{
  "destinationAccountId": 2,
  "amount": 100.00
}
```

## Business Rules

The API enforces several business rules:

- Initial account balance cannot be negative.
- Deposit and withdrawal amounts must be greater than zero.
- Accounts cannot withdraw more money than their available balance.
- Transfers require sufficient funds.
- Source and destination accounts must be different.
- Banking operations are recorded in the transaction history.
- Balance updates and transaction records are executed atomically using database transactions.

## Testing

The service layer is unit tested using JUnit 5 and Mockito.

The tests cover successful operations as well as important business rules, including:

- Deposits
- Withdrawals
- Transfers
- Insufficient balance
- Invalid amounts
- Invalid initial balances
- Transfers to the same account

Repository dependencies are mocked with Mockito so the service business logic can be tested independently from the database.

## Running the Project

### Requirements

- Java 21
- PostgreSQL
- Maven

Create a PostgreSQL database named:

```text
banking_db
```

Configure the database password using the `DB_PASSWORD` environment variable.

The application expects the following configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banking_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Future Improvements

Potential improvements for future versions include:

- Docker support
- Authentication and authorization
- CI/CD pipeline
- Improved API error responses
- Integration tests
- Pagination and filtering
- Redis caching

## Author

Josep Serra