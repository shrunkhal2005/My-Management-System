@echo off
REM Build and run the Docker image for the Spring Boot app
docker build -t jfx-app:latest .
if %ERRORLEVEL% neq 0 (
  echo Docker build failed.
  exit /b %ERRORLEVEL%
)

docker run --rm -p 8080:8080 --name jfx-app jfx-app:latest
