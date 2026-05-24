# JavaFX Spring Boot with MySQL Integration - Setup Guide

## Project Overview
This project integrates JavaFX with Spring Boot 3.2.0 and MySQL database connectivity.

---

## Step 1: Download and Install MySQL

### Option 1: Download MySQL Community Server
1. Visit: https://dev.mysql.com/downloads/mysql/
2. Download the latest MySQL Community Server (8.0.33+)
3. Run the installer and follow the installation wizard
4. Configure MySQL:
   - Set port to: **3306** (default)
   - Set username: **root**
   - Set password: **root** (as per application.properties)

### Option 2: Docker (Alternative - Easier)
If you have Docker installed:
```bash
docker run --name mysql_jfx -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jfx_db -p 3306:3306 -d mysql:8.0
```

### Create Database
After MySQL is running, create the database:
```bash
mysql -u root -p
# Enter password: root

CREATE DATABASE jfx_db;
USE jfx_db;
```

---

## Step 2: Spring Boot Information

### Spring Boot is Already in Your Project
Your `pom.xml` has been updated with:
- **Spring Boot Version**: 3.2.0
- **Spring Boot Starter Web**: For REST APIs
- **Spring Boot Starter Data JPA**: For database operations
- **MySQL Connector**: 8.0.33

### Maven Automatically Downloads Spring Boot
When you run Maven commands, it will automatically download Spring Boot and all dependencies:
```bash
# Build the project (downloads all dependencies including Spring Boot)
mvn clean package

# Or just download dependencies without building
mvn dependency:resolve
```

### Spring Boot Components Included:
✓ Embedded Tomcat Server (port 8080)
✓ Spring Data JPA for ORM
✓ MySQL Database connectivity
✓ Lombok for reducing boilerplate code

---

## Step 3: Build the Project

Navigate to your project directory and run:

```bash
# Clean and build
mvn clean install

# Or just build
mvn clean package
```

This will:
1. Download Spring Boot and all dependencies
2. Compile your Java code
3. Create JAR files in the `target/` directory

---

## Step 4: Update MySQL Connection

Edit [src/main/resources/application.properties](src/main/resources/application.properties):

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/jfx_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root  # Change if your MySQL password is different
```

---

## Step 5: Run the Application

### From IDE (e.g., IntelliJ IDEA, Eclipse, VSCode):
1. Right-click `Main.java`
2. Select "Run as Java Application"
3. The JavaFX window will open with Spring Boot running in the background

### From Terminal:
```bash
mvn spring-boot:run
```

### Or Run the JAR:
```bash
java -jar target/jfx-app-1.0.jar
```

---

## Project Structure

```
d:\jfx\
├── pom.xml                                  # Maven configuration (Spring Boot added)
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Main.java                   # JavaFX + Spring Boot main class
│   │   │   ├── entity/
│   │   │   │   └── User.java               # Database entity
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java     # Database operations
│   │   │   └── service/
│   │   │       └── UserService.java        # Business logic
│   │   └── resources/
│   │       └── application.properties      # Spring Boot configuration
│   └── test/
├── target/                                  # Compiled code and JAR files
└── README.md
```

---

## Database Tables (Auto-Generated)

When the application starts, Hibernate will automatically create the `users` table:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## Key Dependencies Added

| Dependency | Version | Purpose |
|-----------|---------|---------|
| spring-boot-starter-web | 3.2.0 | Web/REST API support |
| spring-boot-starter-data-jpa | 3.2.0 | Database ORM |
| mysql-connector-java | 8.0.33 | MySQL driver |
| lombok | Latest | Reduce boilerplate code |
| javafx-* | 21.0.2 | JavaFX UI framework |

---

## Troubleshooting

### Error: "MySQL Connection refused"
- Make sure MySQL server is running
- Check if port 3306 is listening: `netstat -an | findstr 3306`
- Verify credentials in application.properties

### Error: "No database named jfx_db"
- Create the database manually:
```bash
mysql -u root -p
CREATE DATABASE jfx_db;
```

### Error: "Class not found: com.mysql.cj.jdbc.Driver"
- Run `mvn clean install` to download all dependencies

### Spring Boot not starting
- Ensure Java 21 is installed: `java -version`
- Check if port 8080 is available
- Run with: `mvn spring-boot:run -X` for debug output

---

## Running MySQL Commands

```bash
# Connect to MySQL
mysql -u root -p
# Password: root

# List databases
SHOW DATABASES;

# Select database
USE jfx_db;

# List tables
SHOW TABLES;

# View users table
SELECT * FROM users;

# Insert test data
INSERT INTO users (username, email, password, full_name) 
VALUES ('testuser', 'test@example.com', 'pass123', 'Test User');
```

---

## Next Steps

1. ✓ Update [Main.java](src/main/java/com/example/Main.java) to connect UI buttons to database operations
2. ✓ Create REST API endpoints in a new Controller class
3. ✓ Add authentication using Spring Security
4. ✓ Create additional entities for your business logic

---

## Resources

- **Spring Boot**: https://spring.io/projects/spring-boot
- **MySQL**: https://dev.mysql.com/
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **JavaFX**: https://gluonhq.com/products/javafx/
- **Maven**: https://maven.apache.org/

