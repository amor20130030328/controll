@echo off
echo ================================
echo  Build and Publish Final Release
echo ================================
echo.

set FINAL_DIR=%~dp0final
set BACKEND_EXE=%~dp0controll_service\controll-service.exe
set FRONTEND_BUILD=%~dp0controll_app\build
set TOOLS_DIR=%~dp0tools
set NGINX_DIR=%~dp0controll_app\nginx-1.16.1

echo [1/5] Cleaning final directory...
if exist "%FINAL_DIR%" (
    rmdir /s /q "%FINAL_DIR%"
)
mkdir "%FINAL_DIR%"

echo.
echo [2/5] Checking backend EXE file...
if not exist "%BACKEND_EXE%" (
    echo [ERROR] Backend EXE not found, please run build-backend.bat first
    pause
    exit /b 1
)

echo.
echo [3/5] Checking frontend build files...
if not exist "%FRONTEND_BUILD%\index.html" (
    echo [ERROR] Frontend build not found, please run build-frontend.bat first
    pause
    exit /b 1
)

echo.
echo [4/5] Copying files to final directory...

REM Create directory structure
mkdir "%FINAL_DIR%\backend"
mkdir "%FINAL_DIR%\frontend"
mkdir "%FINAL_DIR%\tools"
mkdir "%FINAL_DIR%\nginx"

REM Copy backend exe
echo   - Copying backend EXE...
copy "%BACKEND_EXE%" "%FINAL_DIR%\backend\" >nul
copy "%~dp0controll_service\controll\target\controll-1.0-SNAPSHOT.jar" "%FINAL_DIR%\backend\" >nul

REM Copy frontend build
echo   - Copying frontend files...
xcopy "%FRONTEND_BUILD%\*" "%FINAL_DIR%\frontend\" /E /I /Q >nul

REM Copy tools
echo   - Copying ADB and scrcpy tools...
xcopy "%TOOLS_DIR%\*" "%FINAL_DIR%\tools\" /E /I /Q >nul

REM Copy Nginx
echo   - Copying Nginx...
xcopy "%NGINX_DIR%\*" "%FINAL_DIR%\nginx\" /E /I /Q >nul

REM Copy startup scripts and docs
echo   - Copying startup scripts and docs...
copy "%~dp0start.bat" "%FINAL_DIR%\" >nul
copy "%~dp0stop.bat" "%FINAL_DIR%\" >nul
copy "%~dp0README.md" "%FINAL_DIR%\" >nul

REM Create simplified startup script
echo   - Creating startup script...
(
echo @echo off
echo echo Starting Device Control System...
echo echo.
echo.
echo REM Start backend
echo start "Backend Service" cmd /k "cd backend && controll-service.exe"
echo timeout /t 5 /nobreak ^>nul
echo.
echo REM Start Nginx
echo cd nginx
echo start /B nginx.exe
echo cd ..
echo.
echo echo System started!
echo echo Access URL: http://localhost
echo echo.
echo start http://localhost
echo pause
) > "%FINAL_DIR%\START.bat"

echo.
echo [5/5] Submitting to GitHub...

REM Check Git
where git >nul 2>&1
if errorlevel 1 (
    echo [WARNING] Git not found, skipping commit
    goto :skip_git
)

REM Add final directory to .gitignore
findstr /C:"final/" "%~dp0.gitignore" >nul 2>&1
if errorlevel 1 (
    echo final/ >> "%~dp0.gitignore"
)

echo   - Checking file size...
powershell -Command "& {$size = (Get-ChildItem '%FINAL_DIR%' -Recurse | Measure-Object -Property Length -Sum).Sum / 1MB; Write-Host ('Final directory size: {0:N2} MB' -f $size)}"

echo.
echo   - Compressing to ZIP file...
powershell -Command "& {Compress-Archive -Path '%FINAL_DIR%\*' -DestinationPath '%~dp0final-release.zip' -Force}"

if exist "%~dp0final-release.zip" (
    echo   - Adding to Git...
    git add final-release.zip .gitignore
    git commit -m "release: Add final release package (compiled frontend and backend)"

    echo   - Pushing to GitHub...
    git push

    if errorlevel 0 (
        echo.
        echo ================================
        echo  Publish Success!
        echo ================================
        echo Local files: %FINAL_DIR%
        echo ZIP package: %~dp0final-release.zip
        echo GitHub: https://github.com/amor20130030328/controll
    ) else (
        echo [WARNING] Git push failed
    )
) else (
    echo [ERROR] Compression failed
)

:skip_git

echo.
echo ================================
echo  Build Complete!
echo ================================
echo.
echo Final files location: %FINAL_DIR%
echo.
echo Directory structure:
echo   final\
echo   +-- backend\
echo   ^|   +-- controll-service.exe
echo   ^|   +-- controll-1.0-SNAPSHOT.jar
echo   +-- frontend\
echo   ^|   +-- index.html + static resources
echo   +-- tools\
echo   ^|   +-- adb\
echo   ^|   +-- scrcpy\
echo   +-- nginx\
echo   +-- START.bat
echo   +-- README.md
echo   +-- ...
echo.
echo Usage: Double-click final\START.bat
echo.
pause
