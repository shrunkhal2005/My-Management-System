@echo off
REM Manual Download Guide - HMS MySQL JDBC Driver
REM Follow these steps to get the driver working

cls
echo.
echo ========================================================================
echo   HMS - MANUAL MYSQL JDBC DRIVER DOWNLOAD GUIDE
echo ========================================================================
echo.
echo Network download failed, but don't worry! Follow these manual steps:
echo.
echo OPTION 1: DIRECT DOWNLOAD (Recommended)
echo ==================================================
echo 1. Open your web browser
echo 2. Go to: https://dev.mysql.com/downloads/connector/j/
echo 3. Look for: "MySQL Connector/J 8.0.33"
echo 4. Click: "Download" on the Platform Independent version
echo 5. It will download: mysql-connector-java-8.0.33.jar.zip or .jar
echo 6. Extract the ZIP file if needed
echo 7. Copy mysql-connector-java-8.0.33.jar to: d:\jfx\lib\
echo.
echo OPTION 2: DIRECT JAR DOWNLOAD LINK
echo ==================================================
echo Copy and paste this URL into your browser:
echo.
echo https://cdn.mysql.com/Downloads/Connector-J/mysql-connector-java-8.0.33.jar
echo.
echo OPTION 3: ALTERNATIVE MIRROR (If above doesn't work)
echo ==================================================
echo Try this Maven repository link:
echo.
echo https://repo1.maven.org/maven2/com/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
echo.
echo OPTION 4: USE MARIADB DRIVER (Drop-in replacement)
echo ==================================================
echo MariaDB JDBC driver is fully compatible. Download from:
echo.
echo https://downloads.mariadb.com/MariaDB/mariadb_java_client-3.1.0.jar
echo.
echo Then save as: d:\jfx\lib\mariadb-java-client-3.1.0.jar
echo.
echo VERIFY INSTALLATION
echo ==================================================
echo After downloading, place the JAR file in: d:\jfx\lib\
echo Then run: Launch-HMS.bat
echo.
echo The Admin Panel should show: "✅ MySQL Connected"
echo.
echo ========================================================================
echo.
pause
