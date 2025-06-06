@echo off
setlocal

REM Script to run the college-schedule-app with Docker Compose
REM Usage: deploy.bat [version]

REM Set default version if not provided
if "%VERSION%"=="" set VERSION=0.3.0-SNAPSHOT

REM Set default GITHUB_OWNER if not provided
if "%GITHUB_OWNER%"=="" set GITHUB_OWNER=chdbc-samples

REM Set default APP_NAME if not provided
if "%APP_NAME%"=="" set APP_NAME=college-schedule-app

echo Deploying %APP_NAME% version: %VERSION%

REM Pull the latest images
docker-compose pull

REM Start the containers
docker-compose up -d

REM Check deployment status
echo Checking deployment status...
timeout /t 10 /nobreak > nul
docker-compose ps

echo.
echo Deployment completed. Application should be available at http://localhost:8080
echo PostgreSQL is available at localhost:5432
echo To view logs: docker-compose logs -f
echo To stop: docker-compose down