# Library Management System

A professional desktop application designed for managing library operations, including book inventory, member registration, book issuing, returns, and automated fine calculation. This system utilizes a robust architecture combining JavaFX for the graphical interface and Hibernate ORM for database persistence.

## Technical Specifications

- **Language**: Java 21+
- **UI Framework**: JavaFX 21
- **ORM Framework**: Hibernate 6.4
- **Database**: Oracle 11g XE (or higher)
- **Build Tool**: Maven

## Key Features

- **Book Management**: Full CRUD (Create, Read, Update, Delete) capabilities for the library inventory, including ISBN tracking and category management.
- **Member Management**: Registration and tracking of library members, including contact details and membership status.
- **Issue and Return System**: Streamlined process for issuing books with automated due date generation and return processing.
- **Automated Fine Calculation**: The system automatically calculates overdue fines at a rate of 5 INR per day.
- **Transaction History**: Comprehensive logging of all library activities, providing a real-time view of current and past transactions.

## Architecture and Design Patterns

The application is built using enterprise-level design patterns to ensure modularity and scalability:

- **Singleton Pattern**: Ensures a single instance of the `DatabaseService` and `HibernateUtil` to manage database connections efficiently.
- **DAO Pattern**: Data Access Objects are abstracted within the service layer to separate business logic from data persistence.
- **Factory Pattern**: The `TransactionFactory` manages the creation of transaction objects based on specific operation types.
- **Observer Pattern**: A `LibraryLogger` monitors and records system events such as book issues and returns in real-time.
- **MVC Pattern**: Clear separation between the UI (JavaFX), the data models (JPA Entities), and the controller logic.

## Database Design

The database schema is fully compliant with Third Normal Form (3NF) to maintain data integrity:

1. **Books Table**: Stores unique identifiers, titles, authors, ISBNs, and stock levels.
2. **Members Table**: Contains personal information, contact details, and account status.
3. **Transactions Table**: Acts as a bridge between Books and Members, maintaining referential integrity through foreign keys and tracking loan periods.

## Installation and Setup

### 1. Database Initialization
Connect to your Oracle Database via SQL*Plus or SQL Developer and execute the setup script:

```bash
sqlplus system/your_password
@database/setup.sql
```

### 2. Configuration
Update the database connection credentials in `src/main/resources/hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.username">system</property>
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```

### 3. Build the Project
Use Maven to compile and install dependencies:

```bash
mvn clean install
```

### 4. Execute the Application
Run the application using the JavaFX Maven plugin:

```bash
mvn javafx:run
```

## Project Structure

- `database/`: Contains SQL scripts for table creation and sample data.
- `src/main/java/com/library/model/`: JPA Entity classes representing the database schema.
- `src/main/java/com/library/service/`: Business logic and service layer operations.
- `src/main/java/com/library/util/`: Utility classes for Hibernate configuration and logging.
- `src/main/resources/`: Configuration files and XML resources.

## Fine Calculation Logic

The system follows a standardized logic for overdue penalties:
- **Loan Period**: 14 Days.
- **Penalty**: 5 INR per day.
- **Formula**: `(Return Date - Due Date) * 5` (Applied only if the return date is after the due date).

## Contributor

Ashvath Parameswaran
