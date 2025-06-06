@echo off
setlocal enabledelayedexpansion

REM Script to build the Docker image for college-schedule-app
REM Usage: build-docker-image.bat [version] [repository]

REM Set default values if not provided
set VERSION=%1
if "%VERSION%"=="" (
    for /f "usebackq delims=" %%a in (`mvn help:evaluate -Dexpression^=project.version -q -DforceStdout -f ..\pom.xml`) do (
        set "VERSION=%%a"
    )
    if "!VERSION!"=="" (
        echo Error: Could not get version from Maven
        exit /b 1
    )
)

set GITHUB_REPOSITORY=%2
if "%GITHUB_REPOSITORY%"=="" (
    for /f "tokens=*" %%a in ('git config --get remote.origin.url ^| find "github.com"') do (
        set GIT_URL=%%a
        set GITHUB_REPOSITORY=!GIT_URL:https://github.com/=!
        set GITHUB_REPOSITORY=!GITHUB_REPOSITORY:.git=!
    )
)

echo Building Docker image for college-schedule-app version: %VERSION%
echo Repository: %GITHUB_REPOSITORY%

REM Copy JAR file from target directory
echo Copying JAR file from ..\target\college-schedule-%VERSION%.jar
copy ..\target\college-schedule-%VERSION%.jar app.jar

REM Check if app.jar exists and was copied successfully
if not exist app.jar (
    echo Error: Failed to copy JAR file from ..\target\college-schedule-%VERSION%.jar
    echo Please make sure to build the project first using 'mvn package'
    exit /b 1
)

REM Build the Docker image
docker build -t ghcr.io/%GITHUB_REPOSITORY%/college-schedule-app:%VERSION% .

REM Optional: Tag as latest as well
docker tag ghcr.io/%GITHUB_REPOSITORY%/college-schedule-app:%VERSION% ghcr.io/%GITHUB_REPOSITORY%/college-schedule-app:latest

echo Docker image built successfully.
echo.
echo To push to GitHub Packages:
echo 1. Make sure you are logged in to GitHub Container Registry:
echo    docker login ghcr.io -u YOUR_GITHUB_USERNAME -p YOUR_GITHUB_PAT
echo.
echo 2. Then run these commands:
echo    docker push ghcr.io/%GITHUB_REPOSITORY%/college-schedule-app:%VERSION%
echo    docker push ghcr.io/%GITHUB_REPOSITORY%/college-schedule-app:latest
echo.
echo Note: You need a GitHub Personal Access Token with 'write:packages' permission.
echo Create one at: https://github.com/settings/tokens