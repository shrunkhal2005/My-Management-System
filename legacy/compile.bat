@echo off
cd /d d:\jfx

REM Download JavaFX SDK if not present
if not exist javafx-sdk (
    echo Downloading JavaFX SDK...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2-windows-x64-sdk.zip', 'javafx-sdk.zip'); Expand-Archive -Path 'javafx-sdk.zip' -DestinationPath 'javafx-sdk' -Force"
)

REM Compile with module path
echo Compiling program.java...
javac --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml program.java

REM Run the program
if %ERRORLEVEL% EQU 0 (
    echo Running program...
    java --module-path javafx-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.graphics,javafx.fxml program
) else (
    echo Compilation failed
)
