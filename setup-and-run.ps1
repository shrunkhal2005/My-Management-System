# JavaFX Setup and Build Script
$ErrorActionPreference = "Continue"

Write-Host "Step 1: Downloading JavaFX SDK..."
$url = "https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2-windows-x64-sdk.zip"
$zipFile = "javafx-sdk.zip"
$extractDir = "javafx-sdk"

if (-not (Test-Path $extractDir)) {
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile($url, $zipFile)
        Write-Host "Downloaded JavaFX SDK"
        
        Expand-Archive -Path $zipFile -DestinationPath $extractDir -Force
        Write-Host "Extracted successfully"
    } catch {
        Write-Host "Error downloading: $_"
        exit 1
    }
}

Write-Host ""
Write-Host "Step 2: Compiling program.java..."
$modulePath = "$extractDir\lib"
$modules = "javafx.application,javafx.scene,javafx.scene.control,javafx.scene.layout,javafx.stage,javafx.geometry"

javac --module-path $modulePath --add-modules $modules program.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed"
    exit 1
}
Write-Host "Compilation successful"

Write-Host ""
Write-Host "Step 3: Running JavaFX application..."
Write-Host ""

java --module-path $modulePath --add-modules $modules program
