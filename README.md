# Hospital Management System (HMS)

A **JavaFX-based Hospital Management System** with **MySQL database integration** using pure JDBC.

## 🚀 Quick Start

### Step 1: Download MySQL JDBC Driver

```powershell
# PowerShell command (run from d:\jfx)
powershell -ExecutionPolicy Bypass -File .\Download-MySQLDriver.ps1
```

**Alternative options:**
- Manual: https://dev.mysql.com/downloads/connector/j/ (Download Connector/J 8.0.33)
- MariaDB: `powershell -ExecutionPolicy Bypass -File .\Download-MySQLDriver.ps1 -UseMariaDB`

### Step 2: Launch HMS

```batch
Launch-HMS.bat
```

### Step 3: Verify Connection

Check Admin Panel to confirm:
- ✅ "MySQL Connected" = Data persists in database
- ⚠️ "OFFLINE MODE" = Data stored in memory only

---

## 📊 Current Status

✅ **Ready:**
- HMS application compiles and runs
- MySQL Server 9.7 running
- Database `jfx_db` created with schema
- All UI features functional

⚠️ **Action Required:**
- Download MySQL JDBC Driver (mysql-connector-java-8.0.33.jar)
- Place in `d:\jfx\lib\` folder

---

## 📁 Files & Commands

| File | Purpose |
|------|---------|
| `Launch-HMS.bat` | Smart launcher (finds driver automatically) |
| `HMS-Status.bat` | Check system status and launch |
| `Download-MySQLDriver.ps1` | Auto-download MySQL driver |
| `HelloFX.java` | Main UI application |
| `DatabaseConnection.java` | JDBC MySQL wrapper |
| `setup.sql` | Database initialization |

**Manual Commands:**
```batch
REM Compile
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml DatabaseConnection.java HelloFX.java

REM Run with MySQL
java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;lib\mysql-connector-java-8.0.33.jar" HelloFX

REM Run offline
java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml HelloFX
```

## Database Credentials

```
Server:   localhost:3306
Database: jfx_db
Username: root
Password: shrunkhal@2005
```

## ONLINE vs OFFLINE Mode

| Feature | ONLINE | OFFLINE |
|---------|--------|---------|
| Data Persists | ✅ Yes (MySQL) | ❌ No (Memory) |
| Multiple Users | ✅ Yes | ❌ No |
| Survives Restart | ✅ Yes | ❌ No |
| MySQL Driver Needed | ✅ Required | ❌ Not needed |

## Troubleshooting

### "Failed to save patient to database"
- **Cause**: Running in OFFLINE MODE (driver missing)
- **Solution**: Download MySQL JDBC Driver

### "Connection refused" error
- **Cause**: MySQL Server not running
- **Solution**: Start MySQL (Services → MySQL Server 9.7)

### "Access denied for user 'root'"
- **Cause**: Wrong credentials
- **Solution**: Check DatabaseConnection.java line 7-8

### JAR file not found after download
- **Cause**: JAR in wrong location
- **Solution**: Ensure file is in `d:\jfx\lib\mysql-connector-java-8.0.33.jar`

## Architecture

**DatabaseConnection.java** - JDBC wrapper handling all MySQL operations:
- `connect()` - Opens connection, creates tables
- `addPatient()` - Insert patient with error messages
- `getAllPatients()` - Query all patients
- `searchPatients()` - Search in database
- `deletePatient()` - Remove record
- `isConnected()` - Connection status

**HelloFX.java** - JavaFX UI with database integration:
- Patient intake form with validation
- Real-time search against database
- Admin panel with connection status
- Patient queue table

## Next Steps

1. **Download Driver**: `powershell -ExecutionPolicy Bypass -File .\Download-MySQLDriver.ps1`
2. **Launch HMS**: `Launch-HMS.bat`
3. **Verify**: Check Admin Panel for "✅ MySQL Connected"
4. **Test**: Add patient and verify data persists after restart

---

**Status**: All components ready - just need MySQL JDBC driver! 🚀

