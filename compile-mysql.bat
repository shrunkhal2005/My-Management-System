@echo off
echo 🚀 Compiling HMS with MySQL JDBC Support...

REM Check if MySQL driver exists in Maven cache
set MYSQL_JAR=%USERPROFILE%\.m2\repository\com\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar

if not exist "%MYSQL_JAR%" (
    echo ❌ MySQL JDBC driver not found in Maven cache
    echo 📥 Downloading MySQL driver via Maven...
    cd /d d:\jfx
    C:\Users\Dell\.maven\maven-3.9.16\bin\mvn dependency:copy-dependencies
    set MYSQL_JAR=d:\jfx\target\dependency\mysql-connector-java-8.0.33.jar
)

if not exist "%MYSQL_JAR%" (
    echo ❌ MySQL driver not found! Setting to alternative path...
    set MYSQL_JAR=d:\jfx\target\dependency\mysql-connector-java-8.0.33.jar
)

REM Compile Java files with JavaFX and MySQL driver
cd /d d:\jfx

echo.
echo 📦 Compiling DatabaseConnection.java...
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MYSQL_JAR%" DatabaseConnection.java

echo 📦 Compiling HelloFX.java...
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MYSQL_JAR%" HelloFX.java

if %ERRORLEVEL% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo 🚀 Running HMS with MySQL Support...
    echo.
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;%MYSQL_JAR%" HelloFX
) else (
    echo ❌ Compilation failed!
    pause
)
