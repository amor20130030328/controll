@echo off
echo ================================
echo  Build Frontend
echo ================================
echo.

REM Change to frontend directory
cd /d "%~dp0controll_app"

REM Check Node.js
where node >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Node.js not found, please install Node.js first
    echo Download: https://nodejs.org/
    pause
    exit /b 1
)

REM Check npm
where npm >nul 2>&1
if errorlevel 1 (
    echo [ERROR] npm not found
    pause
    exit /b 1
)

echo [1/3] Checking dependencies...
if not exist "node_modules" (
    echo Installing dependencies (first build takes longer)...
    call npm install
    if errorlevel 1 (
        echo [ERROR] npm install failed
        pause
        exit /b 1
    )
) else (
    echo Dependencies exist, skipping install
)

echo.
echo [2/3] Cleaning old build files...
if exist "build" (
    rmdir /s /q build
)

echo.
echo [3/3] Building React project...
call npm run build
if errorlevel 1 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo.
echo ================================
echo  Build Complete!
echo ================================
echo Build files location: %~dp0controll_app\build\
echo Entry file: %~dp0controll_app\build\index.html
echo.
pause
