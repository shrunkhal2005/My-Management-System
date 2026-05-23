# Quick Setup and Run Script for JavaFX Spring Boot MySQL Project

Write-Host ""
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "JavaFX Spring Boot MySQL Setup" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mvnCheck = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnCheck) {
    Write-Host "[ERROR] Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Java is installed
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCheck) {
    Write-Host "[ERROR] Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 21 from: https://www.oracle.com/java/technologies/" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[OK] Maven is installed" -ForegroundColor Green
Write-Host "[OK] Java is installed" -ForegroundColor Green
Write-Host ""

# Display Java version
Write-Host "Java Version:" -ForegroundColor Cyan
java -version
Write-Host ""

# Offer to download dependencies
Write-Host ""
Write-Host "Would you like to download all dependencies?" -ForegroundColor Cyan
Write-Host "This includes Spring Boot and MySQL connector." -ForegroundColor Cyan
Write-Host ""

$response = Read-Host "Download dependencies (Y/N)"
if ($response -eq 'Y' -or $response -eq 'y') {
    Write-Host ""
    Write-Host "Downloading dependencies..." -ForegroundColor Cyan
    & mvn dependency:resolve
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Failed to download dependencies" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
    Write-Host "[OK] Dependencies downloaded successfully" -ForegroundColor Green
    Write-Host ""
}

# Build the project
Write-Host "Building the project..." -ForegroundColor Cyan
& mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "[WARNING] Build completed with errors" -ForegroundColor Yellow
}
Write-Host ""

# Display next steps
Write-Host ""
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "NEXT STEPS:" -ForegroundColor Green
Write-Host "1. Install MySQL Server from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor White
Write-Host "2. Create database:" -ForegroundColor White
Write-Host "   mysql -u root -p" -ForegroundColor Gray
Write-Host "   CREATE DATABASE jfx_db;" -ForegroundColor Gray
Write-Host "3. Update MySQL credentials in: src\main\resources\application.properties" -ForegroundColor White
Write-Host "4. Run the application:" -ForegroundColor White
Write-Host "   mvn spring-boot:run" -ForegroundColor Gray
Write-Host ""
Write-Host "Or run this JAR file:" -ForegroundColor White
Write-Host "   java -jar target\jfx-app-1.0.jar" -ForegroundColor Gray
Write-Host ""
Write-Host "For detailed setup instructions, see: MYSQL_SPRINGBOOT_SETUP.md" -ForegroundColor White
Write-Host ""
Read-Host "Press Enter to exit"
