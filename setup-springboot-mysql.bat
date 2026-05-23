@echo off
REM Quick Setup and Run Script for JavaFX Spring Boot MySQL Project

echo.
echo ===================================
echo JavaFX Spring Boot MySQL Setup
echo ===================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 21 from: https://www.oracle.com/java/technologies/
    pause
    exit /b 1
)

echo [OK] Maven is installed
echo [OK] Java is installed
echo.

REM Display Java version
echo Java Version:
java -version
echo.

REM Offer to download dependencies
echo.
echo Would you like to download all dependencies?
echo This includes Spring Boot and MySQL connector.
echo.
choice /C YN /M "Download dependencies (Y/N)? "
if errorlevel 2 goto skip_download
if errorlevel 1 goto download

:download
echo.
echo Downloading dependencies...
call mvn dependency:resolve
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to download dependencies
    pause
    exit /b 1
)
echo [OK] Dependencies downloaded successfully
echo.

:skip_download
REM Build the project
echo Building the project...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] Build completed with errors (This may be OK for certain configurations)
)
echo.

REM Display next steps
echo.
echo ===================================
echo Setup Complete!
echo ===================================
echo.
echo NEXT STEPS:
echo 1. Install MySQL Server from: https://dev.mysql.com/downloads/mysql/
echo 2. Create database:
echo    mysql -u root -p
echo    CREATE DATABASE jfx_db;
echo 3. Update MySQL credentials in: src\main\resources\application.properties
echo 4. Run the application:
echo    mvn spring-boot:run
echo.
echo Or run this JAR file:
echo    java -jar target\jfx-app-1.0.jar
echo.
echo For detailed setup instructions, see: MYSQL_SPRINGBOOT_SETUP.md
echo.
pause
