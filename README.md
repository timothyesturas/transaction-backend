# Transaction Backend

A Spring Boot REST API that reads and stores financial transactions in a CSV file. It is the data service for the [Transaction Frontend](../transaction-frontend/README.md).

- **Base URL:** `http://localhost:8081`
- **Storage:** a CSV file (`./data/transactions.csv`), no database required
- **Stack:** Java 17, Spring Boot 4.1.2, Maven, Apache Commons CSV

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Installation](#2-installation)
3. [Configuration](#3-configuration)
4. [Running the Application](#4-running-the-application)
5. [API Documentation](#5-api-documentation)
6. [Testing](#6-testing)
7. [Troubleshooting](#7-troubleshooting)
8. [Project Structure](#8-project-structure)

---

## 1. Prerequisites

| Software | Version | Purpose | Verify with |
|---|---|---|---|
| Git | Any recent version | Clone the repository | `git --version` |
| JDK | **17** or newer | Compile and run the app | `java -version` |
| Apache Maven | 3.9+ (optional, see note) | Build tool | `mvn -version` |

> **Note:** The project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`). If you do not want to install Maven, use `./mvnw` (macOS/Linux) or `mvnw.cmd` (Windows) in place of `mvn` in every command below.

Make sure `java` is on your system `PATH`. The default port **8081** must be free.

---

## 2. Installation

1. **Clone the repository** (replace the URL with your own):

   ```bash
   git clone <BACKEND_REPOSITORY_URL>
   cd transaction-backend
   ```

2. **Build the project** and download its dependencies:

   ```bash
   mvn clean install
   ```

   Wait for `BUILD SUCCESS`. This compiles the code and runs the tests.

---

## 3. Configuration

All settings live in `src/main/resources/application.properties`:

| Property | Default | Description |
|---|---|---|
| `spring.application.name` | `transaction-backend` | Application name |
| `server.port` | `8081` | Port the API listens on |
| `app.csv.file` | `./data/transactions.csv` | Path to the CSV file used as the data store |

**Overriding values without editing the file** (useful for a different port or CSV location):

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090 --app.csv.file=./data/my-transactions.csv"
```

Or with environment variables:

```bash
# macOS / Linux
SERVER_PORT=9090 APP_CSV_FILE=./data/my-transactions.csv mvn spring-boot:run
```

```powershell
# Windows PowerShell
$env:SERVER_PORT=9090; $env:APP_CSV_FILE=".\data\my-transactions.csv"; mvn spring-boot:run
```

**CSV file**

- If the file does not exist, it is created automatically (with its folder) on startup.
- Required header row and column order:

  ```csv
  Transaction Date,Account Number,Account Holder Name,Amount,Status
  2025-03-01,7289-3445-1121,Maria Johnson,150,Settled
  ```

- Dates must use the `YYYY-MM-DD` format.
- The file must not contain blank rows (such as `,,,,`). See [Troubleshooting](#7-troubleshooting).

**CORS**

The API only accepts browser requests from `http://localhost:4200` and `http://localhost:4201`. To allow other origins, edit the `@CrossOrigin` annotation in `TransactionController.java`.

---

## 4. Running the Application

From the project folder:

```bash
mvn spring-boot:run
```

The backend is ready when the console shows that the Spring Boot application has started on port 8081. Keep this terminal open.

Quick check:

```bash
curl http://localhost:8081/transactions
```

**Stop:** press `CTRL + C` in the terminal.

**Run again later:** `cd` into the project folder and run `mvn spring-boot:run`. No reinstall is needed.

**Run with the frontend:** start the backend first, then follow the [frontend README](../transaction-frontend/README.md). The frontend expects the API at `http://localhost:8081`.

---

## 5. API Documentation

**Content type:** `application/json`
**Authentication:** none

### Transaction object

| Field | Type | Description |
|---|---|---|
| `transactionDate` | string (`YYYY-MM-DD`) | Date of the transaction |
| `accountNumber` | string | Account number, for example `7289-3445-1121` |
| `accountHolderName` | string | Name of the account holder |
| `amount` | number | Transaction amount |
| `status` | string | One of `Pending`, `Settled`, `Failed` |

### Endpoints

| Method | Path | Description | Success |
|---|---|---|---|
| `GET` | `/transactions` | Retrieve all transactions | `200 OK` |
| `POST` | `/transactions` | Add a new transaction | `201 Created` |

---

### `GET /transactions`

Returns every transaction in the CSV file, in file order.

**Request**

```bash
curl http://localhost:8081/transactions
```

**Response: `200 OK`**

```json
[
  {
    "transactionDate": "2025-03-01",
    "accountNumber": "7289-3445-1121",
    "accountHolderName": "Maria Johnson",
    "amount": 150,
    "status": "Settled"
  },
  {
    "transactionDate": "2025-03-02",
    "accountNumber": "1122-3456-7890",
    "accountHolderName": "John Smith",
    "amount": 75.5,
    "status": "Pending"
  }
]
```

An empty data file returns `[]`.

---

### `POST /transactions`

Adds a transaction and appends it to the CSV file.

> The client does **not** send `status`. The server assigns one at random from `Pending`, `Settled`, or `Failed`, and returns it in the response.

**Request body**

| Field | Type | Required |
|---|---|---|
| `transactionDate` | string (`YYYY-MM-DD`) | Yes |
| `accountNumber` | string | Yes |
| `accountHolderName` | string | Yes |
| `amount` | number | Yes |

**Request**

```bash
curl -X POST http://localhost:8081/transactions \
  -H "Content-Type: application/json" \
  -d '{
        "transactionDate": "2025-03-01",
        "accountNumber": "7289-3445-1121",
        "accountHolderName": "Maria Johnson",
        "amount": 150.00
      }'
```

**Response: `201 Created`**

```json
{
  "transactionDate": "2025-03-01",
  "accountNumber": "7289-3445-1121",
  "accountHolderName": "Maria Johnson",
  "amount": 150.00,
  "status": "Pending"
}
```

(`status` will vary between calls.)

**Error responses**

| Status | Cause | Example |
|---|---|---|
| `400 Bad Request` | Malformed JSON, a date that is not `YYYY-MM-DD`, or a non-numeric `amount` | `{"transactionDate": "03/01/2025", ...}` |
| `415 Unsupported Media Type` | `Content-Type: application/json` header missing | |
| `500 Internal Server Error` | The CSV file cannot be read or written | |

> **Known limitation:** the API does not validate that required fields are present. Omitting a field saves an empty value in the CSV, which will cause later `GET` requests to fail. Always send all four fields. The frontend form enforces this for you.

---

## 6. Testing

### Automated tests

```bash
mvn test
```

The included test (`TransactionBackendApplicationTests`) checks that the Spring application context loads successfully. `mvn clean install` also runs it.

### Manual API testing

1. Start the backend (`mvn spring-boot:run`).
2. **List transactions:**

   ```bash
   curl http://localhost:8081/transactions
   ```

   Expected: `200` and a JSON array of the rows in `data/transactions.csv`.
3. **Add a transaction:**

   ```bash
   curl -i -X POST http://localhost:8081/transactions \
     -H "Content-Type: application/json" \
     -d '{"transactionDate":"2025-03-11","accountNumber":"1111-2222-3333","accountHolderName":"Test User","amount":42.50}'
   ```

   Expected: `201 Created` with a `status` of `Pending`, `Settled`, or `Failed`.
4. **Confirm persistence:** run the `GET` request again (the new record is last in the array) and open `data/transactions.csv` (a new row is at the bottom). Restart the app and `GET` again; the record is still there.
5. **Test an error:** send a bad date and expect `400`:

   ```bash
   curl -i -X POST http://localhost:8081/transactions \
     -H "Content-Type: application/json" \
     -d '{"transactionDate":"not-a-date","accountNumber":"1","accountHolderName":"X","amount":1}'
   ```

You can also use Postman or Insomnia with the same URLs and bodies.

---

## 7. Troubleshooting

| Problem | Fix |
|---|---|
| `GET /transactions` returns `500`, or the log shows a date parse error | The CSV contains blank rows (`,,,,`) or a date not in `YYYY-MM-DD`. Delete blank rows, make sure dates use `YYYY-MM-DD`, save the file, and restart. If you edit the file in Excel, format the date column as `YYYY-MM-DD` (Format Cells, Custom) before saving. |
| `Port 8081 was already in use` | Stop the other process, or start with `--server.port=<other port>`. If you change the port, update `apiUrl` in the frontend's `src/app/services/transaction.ts`. |
| Browser shows a CORS error | Run the frontend on port 4200 or 4201, or add your origin to `@CrossOrigin` in `TransactionController.java`. |
| `java` or `mvn` is not recognized | Install JDK 17+ and/or Maven, add them to `PATH`, and reopen the terminal. |
| `Unsupported class file major version` | Your JDK is older than 17. Install JDK 17 or newer. |

---

## 8. Project Structure

```
transaction-backend/
├── data/transactions.csv                  # CSV data store
├── pom.xml                                # Maven build file
├── mvnw, mvnw.cmd                         # Maven Wrapper
└── src/
    ├── main/
    │   ├── java/com/example/transaction_backend/
    │   │   ├── TransactionBackendApplication.java
    │   │   ├── controller/TransactionController.java   # REST endpoints
    │   │   ├── service/TransactionService.java         # Business logic (assigns status)
    │   │   ├── repository/                             # CSV read/write
    │   │   ├── model/Transaction.java                  # Response/storage model
    │   │   └── dto/TransactionRequest.java             # POST request body
    │   └── resources/application.properties
    └── test/java/.../TransactionBackendApplicationTests.java
```
