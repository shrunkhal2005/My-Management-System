# Download MySQL JDBC Driver
# This script will download mysql-connector-java-8.0.33.jar

param(
    [switch]$UseMariaDB = $false
)

Write-Host "========== HMS - MySQL JDBC Driver Downloader ==========" -ForegroundColor Cyan
Write-Host ""

# Create lib directory
$libDir = "d:\jfx\lib"
if (-not (Test-Path $libDir)) {
    Write-Host "Creating $libDir directory..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force -Path $libDir | Out-Null
}

# Download MySQL Connector
if (-not $UseMariaDB) {
    Write-Host "Downloading MySQL Connector/J 8.0.33..." -ForegroundColor Green
    $url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar"
    $output = "$libDir\mysql-connector-java-8.0.33.jar"
    $jarName = "mysql-connector-java-8.0.33.jar"
} else {
    Write-Host "Downloading MariaDB Connector/J 3.1.0..." -ForegroundColor Green
    $url = "https://repo1.maven.org/maven2/org/mariadb/java/mariadb-java-client/3.1.0/mariadb-java-client-3.1.0.jar"
    $output = "$libDir\mariadb-java-client-3.1.0.jar"
    $jarName = "mariadb-java-client-3.1.0.jar"
}

try {
    $ProgressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $url -OutFile $output -TimeoutSec 30 -ErrorAction Stop
    
    if (Test-Path $output) {
        $size = (Get-Item $output).Length / 1MB
        Write-Host "✅ Successfully downloaded: $jarName" -ForegroundColor Green
        Write-Host "   File size: $([math]::Round($size, 2)) MB" -ForegroundColor Green
        Write-Host "   Location: $output" -ForegroundColor Green
        Write-Host ""
        Write-Host "Next step: Run HMS-Status.bat to launch HMS" -ForegroundColor Cyan
    } else {
        Write-Host "❌ Download failed - file not found" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Download failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Alternatives:" -ForegroundColor Yellow
    Write-Host "1. Download manually from: https://dev.mysql.com/downloads/connector/j/" -ForegroundColor Yellow
    Write-Host "2. Use MariaDB driver: $PSCommandPath -UseMariaDB" -ForegroundColor Yellow
    Write-Host "3. Check internet connection" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "=================================================================" -ForegroundColor Cyan
