@echo off
REM Compile and run HMS with MySQL support

echo ========================================
echo HMS - Hospital Management System
echo ========================================
echo.

cd /d d:\jfx

echo [1/2] Compiling DatabaseConnection and HelloFX...
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib ^
  --add-modules javafx.controls,javafx.graphics,javafx.fxml ^
  DatabaseConnection.java HelloFX.java

if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo [2/2] Running HMS...
echo.
echo Database Credentials:
echo - Server: localhost:3306
echo - Database: jfx_db
echo - Username: root
echo - Password: shrunkhal@2005
echo.

REM Try to run with MySQL driver if available
set MYSQL_JAR=C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar

if exist "%MYSQL_JAR%" (
    echo [OK] MySQL driver found - running with database support
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib ^
      --add-modules javafx.controls,javafx.graphics,javafx.fxml ^
      -cp ".;%MYSQL_JAR%" HelloFX
) else (
    echo [INFO] MySQL driver not found - running in offline mode
    echo [INFO] Data will NOT be persisted to MySQL
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib ^
      --add-modules javafx.controls,javafx.graphics,javafx.fxml ^
      HelloFX
)

pause
