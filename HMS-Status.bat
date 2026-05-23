@echo off
setlocal enabledelayedexpansion
cls
echo ╔══════════════════════════════════════════════════════════════════════════════╗
echo ║                   Hospital Management System - Status Check                   ║
echo ╚══════════════════════════════════════════════════════════════════════════════╝
echo.

:: Check if MySQL is running
echo [1/5] Checking MySQL Server...
tasklist | findstr /I "mysqld" >nul
if errorlevel 1 (
    echo ❌ MySQL Server is NOT running
    echo    Start MySQL: Services ^> MySQL80 or MySQL Server 9.7
) else (
    echo ✅ MySQL Server is running
)
echo.

:: Check if MySQL database exists
echo [2/5] Checking HMS Database...
"C:\Program Files\MySQL\MySQL Server 9.7\bin\mysql.exe" -h localhost -u root -pshrunkhal@2005 -e "SELECT 1 FROM information_schema.TABLES WHERE TABLE_SCHEMA='jfx_db' AND TABLE_NAME='patients';" >nul 2>&1
if errorlevel 1 (
    echo ❌ HMS Database (jfx_db) NOT found
    echo    Run setup.sql: mysql -u root -p < setup.sql
) else (
    echo ✅ HMS Database (jfx_db) exists with patients table
)
echo.

:: Check if MySQL JDBC Driver exists
echo [3/5] Checking MySQL JDBC Driver...
if exist "C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar" (
    echo ✅ MySQL JDBC Driver found (mysql-connector-java-8.0.33.jar)
    set DRIVER_FOUND=1
) else (
    echo ⚠️  MySQL JDBC Driver NOT found
    echo    Status: Running in OFFLINE mode (data stored in memory only)
    echo    To fix: Copy mysql-connector-java-8.0.33.jar to lib\ folder
    set DRIVER_FOUND=0
)
echo.

:: Check if JavaFX SDK exists
echo [4/5] Checking JavaFX SDK...
if exist "javafx-sdk\javafx-sdk-21.0.2\lib" (
    echo ✅ JavaFX 21.0.2 SDK found
) else (
    echo ❌ JavaFX SDK not found at javafx-sdk\javafx-sdk-21.0.2\lib
)
echo.

:: Check compilation
echo [5/5] Compiling Java files...
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml DatabaseConnection.java HelloFX.java 2>nul
if errorlevel 1 (
    echo ❌ Compilation FAILED - check Java files for errors
) else (
    echo ✅ Compilation successful
)
echo.

echo ╔══════════════════════════════════════════════════════════════════════════════╗
echo ║                              Running HMS...                                   ║
echo ╚══════════════════════════════════════════════════════════════════════════════╝
echo.

if !DRIVER_FOUND! equ 1 (
    echo Status: ONLINE MODE ^(database operations will save to MySQL^)
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar" HelloFX
) else (
    echo Status: OFFLINE MODE ^(database operations will store data in memory only^)
    echo Note: When you close the application, all entered data will be lost
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml HelloFX
)

echo.
echo Application closed.
pause
