# Database Setup Instructions

## Prerequisites
- Oracle Database 11g or higher installed
- SQL*Plus or SQL Developer

## Setup Steps

### 1. Connect to Oracle Database
```bash
sqlplus system/your_password
```

### 2. Run the Setup Script
```sql
@setup.sql
```

Or if you're in a different directory:
```sql
@path/to/database/setup.sql
```

### 3. Verify Setup
After running the script, you should see:
- ✅ 4 Books inserted
- ✅ 3 Members inserted
- ✅ All tables and sequences created

## Database Configuration

Update the following in `src/main/resources/hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.username">system</property>
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```

## Database Schema

### Normalization (3NF)
- **Books**: Stores book information
- **Members**: Stores member information
- **Transactions**: Links books and members (avoids redundancy)

### Relationships
- Books → Transactions (One-to-Many)
- Members → Transactions (One-to-Many)x`