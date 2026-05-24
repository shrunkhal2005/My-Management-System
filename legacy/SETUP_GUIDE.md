# Hospital Management System - Setup Guide

## Current Status

✅ **Working:**
- HMS Application compiles and runs
- MySQL Server 9.7 is installed and running
- Database `jfx_db` is created with `patients` table
- JavaFX 21.0.2 is configured

⚠️ **Issue:**
- MySQL JDBC Driver (mysql-connector-java-8.0.33.jar) is NOT found
- Running in OFFLINE MODE (data stored in memory only, lost on app close)

## Quick Fix: Get MySQL JDBC Driver

### Option 1: Download Manually (Recommended)
1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Download: **MySQL Connector/J 8.0.33** (Platform Independent)
3. Extract the ZIP file
4. Find `mysql-connector-java-8.0.33.jar`
5. Copy it to one of these locations:
   - `d:\jfx\lib\mysql-connector-java-8.0.33.jar` (recommended)
   - `C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\`

### Option 2: Download via PowerShell (If Option 1 doesn't work)
```powershell
# Create lib folder
mkdir -Force d:\jfx\lib

# Download JAR directly (8.0.33)
$url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar"
$output = "d:\jfx\lib\mysql-connector-java-8.0.33.jar"

# Download with progress
$ProgressPreference = 'SilentlyContinue'
Invoke-WebRequest -Uri $url -OutFile $output
Write-Host "✅ Downloaded to: $output"
```

### Option 3: Use Alternative Driver (MariaDB)
If downloading is not possible, use MariaDB driver (drop-in replacement):
```powershell
$url = "https://repo1.maven.org/maven2/org/mariadb/java/mariadb-java-client/3.1.0/mariadb-java-client-3.1.0.jar"
$output = "d:\jfx\lib\mariadb-java-client-3.1.0.jar"
Invoke-WebRequest -Uri $url -OutFile $output
```

Then modify the classpath in the batch file.

## Verify Installation

After copying the JAR file:

```powershell
# Check if file exists
Test-Path "d:\jfx\lib\mysql-connector-java-8.0.33.jar"
# Should return: True

# Check file size (should be ~2-4 MB)
(Get-Item "d:\jfx\lib\mysql-connector-java-8.0.33.jar").Length / 1MB
```

## Running HMS

### With Driver (ONLINE MODE)
```powershell
cd d:\jfx
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml DatabaseConnection.java HelloFX.java

java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;d:\jfx\lib\mysql-connector-java-8.0.33.jar" HelloFX
```

### Without Driver (OFFLINE MODE)
```powershell
cd d:\jfx
java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml HelloFX
```

## Understanding Modes

### ONLINE MODE (With MySQL Driver)
- ✅ Data persists in MySQL
- ✅ Can access data after closing/reopening
- ✅ Multiple users can share data
- Console shows: `✅ MySQL JDBC Driver loaded successfully`

### OFFLINE MODE (Without MySQL Driver)
- ⚠️ Data stored in application memory only
- ⚠️ Data lost when closing application
- ⚠️ Single user only
- Console shows: `⚠️ MySQL JDBC Driver not in classpath`
- Admin Panel shows: `⚠️ OFFLINE MODE (MySQL Driver Missing)`

## Troubleshooting

### "Failed to save patient to database"
- **Cause:** Running in OFFLINE MODE (JDBC driver missing)
- **Fix:** Install MySQL JDBC driver (see Quick Fix above)

### "Connection refused" in ONLINE MODE
- **Cause:** MySQL Server not running
- **Fix:** Start MySQL:
  - Windows: Services → MySQL Server 9.7 → Right-click → Start
  - Or: `net start MySQL80`

### "Access denied" error
- **Cause:** Wrong credentials
- **Fix:** Check DatabaseConnection.java - verify username is `root` and password is `shrunkhal@2005`

### JAR file is corrupted
- **Cause:** Download incomplete or wrong version
- **Fix:** Delete and re-download mysql-connector-java-8.0.33.jar

## Database Details

**MySQL Connection String:**
```
jdbc:mysql://localhost:3306/jfx_db
Username: root
Password: shrunkhal@2005
```

**Patients Table Schema:**
```sql
CREATE TABLE patients (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    phone VARCHAR(20),
    department VARCHAR(50),
    doctor VARCHAR(100),
    priority VARCHAR(20),
    time VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Next Steps

1. ✅ Download mysql-connector-java-8.0.33.jar
2. ✅ Place it in `d:\jfx\lib\`
3. ✅ Run `HMS-Status.bat` to verify everything is set up
4. ✅ Launch HMS - you should see "✅ MySQL Connected" in Admin Panel
5. ✅ Add a patient and verify it shows in `Admin Panel > Refresh Status`

## Files in This Project

| File | Purpose |
|------|---------|
| `HelloFX.java` | Main JavaFX UI Application |
| `DatabaseConnection.java` | JDBC wrapper for MySQL operations |
| `setup.sql` | Database initialization script |
| `HMS-Status.bat` | Status check and launcher |
| `compile.bat` | Compile everything |
| `run-summary.bat` | Show what's happening |
| `pom.xml` | Maven configuration (optional) |

---

**Questions?** Check console output when HMS launches - it will tell you exactly what's happening.
