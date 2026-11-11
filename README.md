TRANSACTION BACKEND
===================

STEP 1: INSTALL THE REQUIRED SOFTWARE
-------------------------------------

Before running the application, install the following programs:

1. Git
   - Download Git from https://git-scm.com/downloads.
   - Run the installer.
   - Follow the installation instructions.

2. Java Development Kit (JDK) 17
   - Download and install JDK 17.
   - Make sure Java is added to your system PATH.

3. Apache Maven
   - Download Maven from https://maven.apache.org/download.cgi.
   - Extract the downloaded archive to a folder on your computer.
   - Add Maven's bin folder to your system PATH.

STEP 2: VERIFY THE INSTALLATIONS
--------------------------------

Open Command Prompt and run each command:

    git --version

    java --version

    mvn --version

Each command should display its installed version. If a command is not recognized, check that the program is installed and its folder has been added to PATH.

STEP 3: CLONE THE BACKEND REPOSITORY
------------------------------------

1. Open Command Prompt.
2. Navigate to the folder where you want to save the project. For example:

    cd C:\\Projects

3. Clone the backend repository. Replace the URL below with the actual GitHub repository URL:

    git clone <BACKEND_REPOSITORY_URL>

4. Open the project folder:

    cd transaction-backend

STEP 4: BUILD THE APPLICATION
-----------------------------

In the project folder, run:

    mvn clean install

Wait for Maven to finish. This command downloads the required project dependencies, compiles the source code, and runs the configured tests.

If the build completes successfully, Maven displays BUILD SUCCESS.

STEP 5: RUN THE BACKEND APPLICATION
-----------------------------------

In the same project folder, run:

    mvn spring-boot:run

Wait for the application to start. The backend is running when the Spring Boot startup messages appear and the application reports that it has started.

STEP 6: ACCESS THE BACKEND
--------------------------

Open a web browser or API testing tool and use:

    http://localhost:8081

To retrieve all transactions, send a GET request to:

    http://localhost:8081/transactions

To add a transaction, send a POST request to:

    http://localhost:8081/transactions

Set the request header:

    Content-Type: application/json

Use this sample JSON request body:

    {
        "transactionDate": "2025-03-01",
        "accountNumber": "7289-3445-1121",
        "accountHolderName": "Maria Johnson",
        "amount": 150.00,
        "status": "Pending"
    }

STEP 7: STOP THE APPLICATION
----------------------------

1. Return to the Command Prompt window where the backend is running.
2. Press CTRL + C.
3. If Command Prompt asks you to terminate the process, type Y and press ENTER.

The backend application will stop.

STEP 8: RUN THE APPLICATION AGAIN
---------------------------------

Whenever you want to run the backend again:

1. Open Command Prompt.
2. Navigate to the backend project folder. For example:

    cd C:\\Projects\\transaction-backend

3. Start the application:

    mvn spring-boot:run

Keep the Command Prompt window open while using the backend.
