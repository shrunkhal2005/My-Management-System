# Quick Reference Guide - JavaFX Spring Boot MySQL

## What Was Added to Your Project

### ✅ Dependencies Added (in pom.xml)
- **Spring Boot 3.2.0** - Full-stack framework for Java applications
- **Spring Data JPA** - Database ORM (Object-Relational Mapping)
- **MySQL Connector 8.0.33** - MySQL database driver
- **Lombok** - Reduces boilerplate Java code
- **Spring Boot Test** - Testing framework

### ✅ New Classes Created
1. **User.java** (`entity/`) - Database entity representing users
2. **UserRepository.java** (`repository/`) - Database queries interface
3. **UserService.java** (`service/`) - Business logic layer
4. **UserController.java** (`controller/`) - REST API endpoints
5. **Main.java** (updated) - Now runs Spring Boot + JavaFX

### ✅ Configuration Files
- **application.properties** - MySQL connection settings

---

## 🚀 Quick Start (5 Steps)

### Step 1: Download Spring Boot (Automatic)
Maven will automatically download Spring Boot when you run:
```bash
mvn clean install
```

### Step 2: Install MySQL

**Option A - Traditional Installation:**
- Download: https://dev.mysql.com/downloads/mysql/
- Install and start the MySQL service
- Default: user=root, password=root, port=3306

**Option B - Docker (Recommended):**
```bash
docker run --name mysql_jfx -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jfx_db -p 3306:3306 -d mysql:8.0
```

### Step 3: Create Database
```bash
mysql -u root -p
# Password: root

CREATE DATABASE jfx_db;
```

### Step 4: Build Project
```bash
mvn clean install
```
This downloads all dependencies including Spring Boot (automatically).

### Step 5: Run Application
```bash
mvn spring-boot:run
```

---

## 📝 Key Files & Their Purpose

| File | Purpose |
|------|---------|
| `pom.xml` | Maven configuration with Spring Boot dependencies |
| `src/main/resources/application.properties` | MySQL connection settings |
| `src/main/java/com/example/Main.java` | JavaFX UI + Spring Boot entry point |
| `src/main/java/com/example/entity/User.java` | Database user entity |
| `src/main/java/com/example/repository/UserRepository.java` | Database queries |
| `src/main/java/com/example/service/UserService.java` | Business logic |
| `src/main/java/com/example/controller/UserController.java` | REST API endpoints |

---

## 🗄️ Database Operations

### Create User (Java Code)
```java
User user = new User();
user.setUsername("john_doe");
user.setEmail("john@example.com");
user.setPassword("secure_password");
user.setFullName("John Doe");

userService.saveUser(user);
```

### Query Users
```java
// Get all users
List<User> allUsers = userService.getAllUsers();

// Get by username
Optional<User> user = userService.getUserByUsername("john_doe");

// Get by email
Optional<User> user = userService.getUserByEmail("john@example.com");

// Get by ID
Optional<User> user = userService.getUserById(1L);
```

### Update User
```java
User user = userService.getUserById(1L).orElse(null);
user.setEmail("newemail@example.com");
userService.updateUser(user);
```

### Delete User
```java
userService.deleteUser(1L);
```

---

## 🌐 REST API Endpoints

The application exposes these API endpoints:

### Get All Users
```
GET http://localhost:8080/api/users
```
Response:
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "password": "secure_password",
    "fullName": "John Doe",
    "createdAt": "2024-01-15 10:30:00"
  }
]
```

### Get User by ID
```
GET http://localhost:8080/api/users/1
```

### Get User by Username
```
GET http://localhost:8080/api/users/username/john_doe
```

### Create User
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "secure_password",
  "fullName": "John Doe"
}
```

### Update User
```
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "username": "john_doe",
  "email": "newemail@example.com",
  "password": "secure_password",
  "fullName": "John Doe"
}
```

### Delete User
```
DELETE http://localhost:8080/api/users/1
```

---

## 🔧 Configuration

### MySQL Connection (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jfx_db
spring.datasource.username=root
spring.datasource.password=root
```

### Server Port
```properties
server.port=8080
```

### Database Operations
```properties
spring.jpa.hibernate.ddl-auto=update  # auto-create/update tables
```

---

## 📊 Database Table Structure

**Table: users**
```
┌─────────────┬──────────────┬────────┐
│ Column      │ Type         │ Notes  │
├─────────────┼──────────────┼────────┤
│ id          │ BIGINT       │ PRIMARY KEY, AUTO_INCREMENT |
│ username    │ VARCHAR(50)  │ UNIQUE, NOT NULL |
│ email       │ VARCHAR(100) │ NOT NULL |
│ password    │ VARCHAR(255) │ NOT NULL |
│ full_name   │ VARCHAR(100) │ Optional |
│ created_at  │ TIMESTAMP    │ Auto-set to NOW() |
└─────────────┴──────────────┴────────┘
```

---

## 🐛 Troubleshooting

| Error | Solution |
|-------|----------|
| MySQL connection refused | Make sure MySQL is running and port 3306 is open |
| No database named jfx_db | Run: `CREATE DATABASE jfx_db;` in MySQL |
| Maven not found | Install Maven: https://maven.apache.org/download.cgi |
| Java version error | Update to Java 21: https://www.oracle.com/java/technologies/ |
| Port 8080 in use | Change port in application.properties: `server.port=8081` |

---

## 📚 Useful Commands

```bash
# Download all dependencies (including Spring Boot)
mvn dependency:resolve

# Clean and build the project
mvn clean package

# Run the application
mvn spring-boot:run

# Run tests
mvn test

# Check for outdated dependencies
mvn versions:display-outdated

# Create an executable JAR
mvn clean package -DskipTests

# Run the JAR
java -jar target/jfx-app-1.0.jar
```

---

## 🎯 Next Steps

1. **Connect JavaFX UI to Database**
   - Update button click handlers to call UserService
   - Display results in JavaFX ListView/TableView

2. **Add More Entities**
   - Create additional entities for your business logic
   - Add corresponding repositories and services

3. **Implement Authentication**
   - Add Spring Security for user authentication
   - Hash passwords using BCrypt

4. **Add Frontend Validation**
   - Validate email format
   - Check password strength
   - Display error messages

5. **Deploy to Cloud**
   - Package as Docker container
   - Deploy to Azure App Service
   - Use Azure Database for MySQL

---

## 📖 Resources

- **Spring Boot Guide**: https://spring.io/guides/gs/spring-boot/
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **MySQL Documentation**: https://dev.mysql.com/doc/
- **JavaFX Tutorial**: https://gluonhq.com/products/javafx/
- **Maven Documentation**: https://maven.apache.org/

---

## 💾 Directory Structure

```
d:\jfx\
├── pom.xml                               # Maven config with Spring Boot
├── MYSQL_SPRINGBOOT_SETUP.md            # Detailed setup guide
├── QUICK_REFERENCE.md                   # This file
├── setup-springboot-mysql.bat           # Quick setup batch script
├── setup-springboot-mysql.ps1           # Quick setup PowerShell script
│
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Main.java                # Entry point (Spring Boot + JavaFX)
│   │   │   ├── controller/
│   │   │   │   └── UserController.java  # REST API endpoints
│   │   │   ├── entity/
│   │   │   │   └── User.java            # Database entity
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java  # Database queries
│   │   │   └── service/
│   │   │       └── UserService.java     # Business logic
│   │   │
│   │   └── resources/
│   │       └── application.properties   # Spring Boot configuration
│   │
│   └── test/
│       └── java/...                     # Unit tests
│
└── target/                              # Compiled output
    └── jfx-app-1.0.jar                 # Executable JAR file
```

---

**Status**: ✅ MySQL Integration & Spring Boot Setup Complete!

