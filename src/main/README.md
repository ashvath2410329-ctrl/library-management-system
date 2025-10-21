# Library Management System

A desktop application for managing library operations including book inventory, member management, book issuing, returns, and automatic fine calculation.

## Project Information
- **Course**: DBMS & Java Programming Mini Project
- **Database**: Oracle 11g XE
- **Framework**: JavaFX 21
- **ORM**: Hibernate 6.4
- **Build Tool**: Maven

## Features
- Book Management (Add, View, Update, Delete, Search)
- Member Management (Add, View, Update, Delete)
- Issue Books to Members
- Return Books with Automatic Fine Calculation (₹5 per day overdue)
- View All Transactions History
- Real-time Database Synchronization

## Database Design

### Normalization (3NF Compliance)
The database follows Third Normal Form (3NF):
- **1NF**: All tables have atomic values with no repeating groups
- **2NF**: No partial dependencies - all non-key attributes depend on the entire primary key
- **3NF**: No transitive dependencies - separated Transactions from Books/Members

### Tables
1. **Books**: Stores book information (ID, Title, Author, ISBN, Category, Copies)
2. **Members**: Stores library member details (ID, Name, Email, Phone, Address, Status)
3. **Transactions**: Links books and members (ID, Book_ID, Member_ID, Issue/Due/Return Dates, Fine, Status)

### Relationships
- Books → Transactions (One-to-Many)
- Members → Transactions (One-to-Many)
- Foreign Key constraints ensure referential integrity

## Prerequisites
- Java JDK 21 or higher
- Apache Maven 3.6+
- Oracle Database 11g or higher
- IntelliJ IDEA (recommended) or any Java IDE

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd library-management
```

### 2. Database Setup
Open SQL*Plus or SQL Developer and run:
```bash
sqlplus system/your_password
@database/setup.sql
```

This will:
- Create all required tables (Books, Members, Transactions)
- Create sequences for auto-increment IDs
- Insert sample data for testing

### 3. Configure Database Connection
Edit `src/main/resources/hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.username">system</property>
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```
Change `YOUR_PASSWORD` to your Oracle password.

### 4. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn javafx:run
```

Or in IntelliJ:
- Maven Panel (right side) → Plugins → javafx → javafx:run (double-click)

## Project Structure
```
library-management/
├── database/
│   └── setup.sql              # Database initialization script
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   ├── Main.java      # Application entry point & UI
│   │   │   ├── model/         # Entity classes (Book, Member, Transaction)
│   │   │   ├── service/       # DatabaseService (business logic)
│   │   │   └── util/          # HibernateUtil (DB connection)
│   │   └── resources/
│   │       └── hibernate.cfg.xml  # Hibernate configuration
├── pom.xml                    # Maven dependencies
└── README.md
```

## Design Patterns Used
- **Singleton Pattern**: `DatabaseService`, `HibernateUtil` - ensures single instance
- **DAO Pattern**: Database operations abstracted in service layer
- **Entity Pattern**: JPA entities with Hibernate annotations
- **MVC Pattern**: Separation of UI (JavaFX) and business logic

## Usage

### Managing Books
1. Click "Manage Books"
2. View all books in the table
3. Add new books using "Add Book" button
4. Delete books by selecting and clicking "Delete Selected"
5. Refresh to see latest data

### Managing Members
1. Click "Manage Members"
2. Add new members with name, email, phone, and address
3. View all registered members
4. Delete inactive members

### Issuing Books
1. Click "Issue Book"
2. Select a book from dropdown (only shows available books)
3. Select a member
4. Click "Issue Book"
5. Due date is automatically set to 14 days from issue date

### Returning Books
1. Click "Return Book"
2. View all active (unreturned) transactions
3. Select the transaction to return
4. Click "Return Selected Book"
5. Fine is automatically calculated if overdue (₹5/day)

### Viewing Transactions
- Click "View Transactions" to see complete history
- Shows issue date, due date, return date, fine amount, and status

## Fine Calculation Logic
```java
overdue_days = return_date - due_date
if (overdue_days > 0) {
    fine = overdue_days × ₹5
}
```

## Technologies Used
- **Java 25**: Core programming language
- **JavaFX 21**: Desktop UI framework
- **Hibernate 6.4**: ORM for database operations
- **Oracle 11g XE**: Relational database
- **Maven**: Dependency management and build automation
- **JPA Annotations**: For entity mapping

## Troubleshooting

### "JavaFX runtime components are missing"
Use Maven to run: `mvn javafx:run`

### "Connection refused" or database errors
- Ensure Oracle database is running
- Verify credentials in `hibernate.cfg.xml`
- Check if port 1521 is accessible

### Maven warnings on JDK 25
These are harmless compatibility warnings from older libraries. Application works perfectly.

## Future Enhancements
- Search functionality for books and members
- Book reservation system
- Email notifications for due dates
- Reports generation (overdue books, popular books)
- User authentication for librarians

## Contributors
- Ashvath Parameswaran - 3122245002009

## License
This project is created for educational purposes as part of DBMS and Java Programming courses.
```

**Save this file.** ✅

---

## **Step 33: Create .gitignore**

1. Right-click on project root
2. New → File
3. Name it: `.gitignore`
4. Paste:
```
# Compiled class files
*.class
target/

# IDE files
.idea/
*.iml
*.iws
*.ipr

# Maven
.mvn/

# Database
*.db

# OS files
.DS_Store
Thumbs.db

# Logs
*.log
