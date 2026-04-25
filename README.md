# Restaurant Ordering System

A comprehensive, multi-module restaurant ordering and management system. The application is divided into a centralized Java backend with desktop GUI clients for restaurant staff, and a web-based frontend application for handling orders.

## System Architecture
The system operates on a client-server architecture with a shared database:
1. **Central Server (`objednavkysystem`):** Handles core business logic, database communication, and serves as the backbone of the application.
2. **Staff Desktop Clients (`objednavkysystem`):** Three distinct Java GUI applications for restaurant operations:
   - **Admin Panel:** For management, menu updates, and system overview.
   - **Kitchen Panel:** For chefs to view and manage incoming food orders.
   - **Pickup Panel:** For dispatching ready orders to customers.
3. **Web Order Panel (`orderpanel`):** A Servlet/JSP-based web application acting as the customer-facing endpoint for placing orders and generating receipts.

## Project Structure
The repository consists of two main Maven projects:

### 1. `objednavkysystem` (Backend & GUI)
- `src/main/java/cz/common/`: Shared resources, database utilities (`db`), data models, and services.
- `src/main/java/cz/server/`: The core server application.
- `src/main/java/cz/okna/`: Source code for the three desktop client applications (`adminpanel`, `kitchenpanel`, `pickuppanel`).

### 2. `orderpanel` (Customer Web App)
- `src/main/java/uctenky/`: Logic for receipt processing and generation.
- `src/main/webapp/`: Web resources, JSP pages, and `WEB-INF` configuration.
- *Note: This module depends on the compiled `objednavkysystem` artifact.*
- 
## Prerequisites

- **Java Development Kit (JDK):** Version 17.
- **Maven:** For dependency management and building.
- **MySQL Database:** A running MySQL server for data persistence.
- **Servlet Container:** Apache Tomcat (version 9 or 10 recommended) to host the `orderpanel` web application.

## Database
**Important:** This repository contains the application source code and data models, but it **does not include a pre-configured database schema or SQL initialization script**. 

To run the system, you must:
1. Set up your own MySQL instance.
2. Create the necessary tables and schema based on the entities.
3. Update the database connection credentials (URL, username, password) directly in the `SqlWrapper` class, which is located in the `cz.common.db` package.

## Build and Installation

Before running the applications, you must build the projects in the correct order using Maven. From the root directory of the workspace:

1. **Build and install the backend module first:**
```bash
cd objednavkysystem
mvn clean install
```

2. **Build the web application:**
```bash
cd ../orderpanel
mvn clean package
```

## How to Run
To ensure the system works correctly, start the components in the following order:

### Step 1: Database Setup
Ensure your MySQL server is running. You may need to update the database connection credentials (URL, username, password) located in the database configuration files inside `objednavkysystem/src/main/java/cz/common/db/SqlWrapper.java`.

### Step 2: Start the Central Server
Open a terminal in the `objednavkysystem` directory and run the main server class using Maven:
```bash
mvn exec:java "-Dexec.mainClass=cz.server.Server"
```

### Step 3: Desktop/Web Clients
Open a new terminal in the `objednavkysystem` directory for each client application you wish to start:

**Admin Panel:**
```bash
mvn exec:java "-Dexec.mainClass=cz.okna.adminpanel.App"
```
**Kitchen Panel:**
```bash
mvn exec:java "-Dexec.mainClass=cz.okna.kitchenpanel.App"
```
**Pickup Panel:**
```bash
mvn exec:java "-Dexec.mainClass=cz.okna.pickuppanel.App"
```
**Web Application:**
1. Locate the built WAR file at `orderpanel/target/orderpanel.war`.
2. Copy this file to the `webapps` directory of your Apache Tomcat installation.
3. Start Tomcat. The application will typically be accessible at `http://localhost:8080/orderpanel`.


## Notes
- The web module includes JSP files under `src/main/webapp`.
- Database connection details and servlet configuration may need adjustment for your environment.

The source code and GUI of this project are in Czech, as it was originally built for a local audience. This English README is provided to explain the app's logic and features to a broader audience.