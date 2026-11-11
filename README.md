# Transaction Backend

## Prerequisites

Make sure the following tools are installed:

- Git
- Java JDK 17
- Apache Maven

Verify the installations:

```bash
git --version
java --version
mvn --version
```

## Installation and Setup

### Step 1. Clone the Repository

```bash
git clone <BACKEND_REPOSITORY_URL>
```

### Step 2. Navigate to the Project Directory

```bash
cd transaction-backend
```

### Step 3. Build the Project

```bash
mvn clean install
```

Wait for the build to finish successfully.

### Step 4. Run the Application

```bash
mvn spring-boot:run
```

### Step 5. Access the Backend API

Once the application has started successfully, the backend will be accessible at:

```text
http://localhost:8081
```

### Step 6. Verify the Application

Open Postman or another API testing tool.

**Get All Transactions**

- Method: `GET`
- URL: `http://localhost:8081/transactions`

**Add a Transaction**

- Method: `POST`
- URL: `http://localhost:8081/transactions`
- Header: `Content-Type: application/json`
- Body: `raw` → `JSON`

Sample Request Body:

```json
{
    "transactionDate": "2025-03-01",
    "accountNumber": "7289-3445-1121",
    "accountHolderName": "Maria Johnson",
    "amount": 150.00,
    "status": "Pending"
}
```

Make sure the JSON field names and values match the `TransactionRequest` DTO.

## Stopping the Application

To stop the running Spring Boot application:

1. Go to the terminal where the application is running.
2. Press `CTRL + C`.
3. Wait for the application to shut down.

## Available Commands

| Command | Description |
|---|---|
| `mvn clean` | Clean previous build files. |
| `mvn compile` | Compile the application. |
| `mvn test` | Run automated tests. |
| `mvn clean install` | Build and package the application. |
| `mvn spring-boot:run` | Run the application. |
