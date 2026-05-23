@echo off
REM Hospital Management System - Quick Launcher
REM This script attempts to find or use MySQL JDBC driver

setlocal enabledelayedexpansion
cls

echo.
echo ╔═══════════════════════════════════════════════════════════════════╗
echo ║      HOSPITAL MANAGEMENT SYSTEM - LAUNCHER                        ║
echo ╚═══════════════════════════════════════════════════════════════════╝
echo.

REM Check for driver in local lib folder first
if exist "lib\mysql-connector-java-8.0.33.jar" (
    echo [✅] Found MySQL driver in: lib\mysql-connector-java-8.0.33.jar
    set DRIVER=lib\mysql-connector-java-8.0.33.jar
    set MODE=ONLINE
    goto :run
)

REM Check for driver in Maven repository
if exist "C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar" (
    echo [✅] Found MySQL driver in: C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\
    set DRIVER=C:\Users\Dell\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar
    set MODE=ONLINE
    goto :run
)

REM Driver not found
echo [⚠️] MySQL JDBC Driver NOT FOUND
echo.
echo To download the driver, run one of:
echo   powershell -ExecutionPolicy Bypass -File .\Download-MySQLDriver.ps1
echo   OR
echo   powershell -ExecutionPolicy Bypass -File .\Download-MySQLDriver.ps1 -UseMariaDB
echo.
echo Or download manually from:
echo   https://dev.mysql.com/downloads/connector/j/
echo.
echo Continuing in OFFLINE MODE (data not persistent)...
echo.
timeout /t 3 >nul
set MODE=OFFLINE
goto :run

:run
echo.
echo Starting HMS in %MODE% mode...
echo.

if "%MODE%"=="ONLINE" (
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib ^
         --add-modules javafx.controls,javafx.graphics,javafx.fxml ^
         -cp ".;!DRIVER!" HelloFX
) else (
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib ^
         --add-modules javafx.controls,javafx.graphics,javafx.fxml ^
         HelloFX
)

echo.
echo Application closed.
