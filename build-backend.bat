@echo off
echo ================================
echo  Build Backend to EXE
echo ================================
echo.

REM Change to backend directory
cd /d "%~dp0controll_service"

REM Check Maven
where mvn >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven not found, please install Maven first
    echo Download: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo [1/3] Cleaning old build files...
call mvn clean

echo.
echo [2/3] Building Java project and packaging JAR...
call mvn package -DskipTests
if errorlevel 1 (
    echo [ERROR] Maven build failed
    pause
    exit /b 1
)

echo.
echo [3/3] Generating EXE file...

REM Check if launch4j exists
set LAUNCH4J=C:\launch4j\launch4jc.exe
if not exist "%LAUNCH4J%" (
    set LAUNCH4J=%TEMP%\launch4j\launch4jc.exe
)

if not exist "%LAUNCH4J%" (
    echo [WARNING] launch4j not found, downloading...

    REM Download launch4j
    powershell -Command "& {Invoke-WebRequest -Uri 'https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.zip/download' -OutFile '%TEMP%\launch4j.zip'}"

    REM Extract
    powershell -Command "& {Expand-Archive -Path '%TEMP%\launch4j.zip' -DestinationPath '%TEMP%' -Force}"

    set LAUNCH4J=%TEMP%\launch4j\launch4jc.exe
)

REM Generate exe with launch4j
"%LAUNCH4J%" "%~dp0controll_service\launch4j-config.xml"
if errorlevel 1 (
    echo [ERROR] EXE generation failed
    pause
    exit /b 1
)

echo.
echo ================================
echo  Build Complete!
echo ================================
echo EXE file: %~dp0controll_service\controll-service.exe
echo JAR file: %~dp0controll_service\controll\target\controll-1.0-SNAPSHOT.jar
echo.
pause
