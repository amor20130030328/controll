@echo off
echo ================================
echo  Build All - Automated Build System
echo ================================
echo.
echo This script will:
echo 1. Build Java backend to EXE
echo 2. Build React frontend
echo 3. Package and upload to GitHub
echo.
echo Press any key to start, or Ctrl+C to cancel...
pause >nul

echo.
echo ================================
echo  Step 1/3: Build Backend
echo ================================
call "%~dp0build-backend.bat"
if errorlevel 1 (
    echo [ERROR] Backend build failed
    pause
    exit /b 1
)

echo.
echo ================================
echo  Step 2/3: Build Frontend
echo ================================
call "%~dp0build-frontend.bat"
if errorlevel 1 (
    echo [ERROR] Frontend build failed
    pause
    exit /b 1
)

echo.
echo ================================
echo  Step 3/3: Package and Publish
echo ================================
call "%~dp0build-and-publish.bat"

echo.
echo ================================
echo  All Done!
echo ================================
pause
