@echo off
chcp 65001 >nul
echo ================================
echo  设备控制系统启动脚本
echo ================================
echo.

REM 设置Java环境
set JAVA_HOME=%~dp0java\jre
set PATH=%JAVA_HOME%\bin;%PATH%

REM 设置Node环境
set NODE_HOME=%~dp0node
set PATH=%NODE_HOME%;%PATH%

REM 检查Java是否可用
"%JAVA_HOME%\bin\java.exe" -version >nul 2>&1
if errorlevel 1 (
    echo [错误] Java环境配置失败
    pause
    exit /b 1
)

REM 检查Node是否可用
"%NODE_HOME%\node.exe" -v >nul 2>&1
if errorlevel 1 (
    echo [错误] Node.js环境配置失败
    pause
    exit /b 1
)

echo [1/3] 启动Spring Boot后端服务...
cd /d "%~dp0controll_service"
start "Backend Service" cmd /k "%JAVA_HOME%\bin\java.exe" -jar "controll\target\controll-1.0-SNAPSHOT-jar-with-dependencies.jar"
cd /d "%~dp0"
echo 后端服务已启动 (端口8080)
timeout /t 5 /nobreak >nul

echo [2/3] 启动Node.js辅助服务...
cd /d "%~dp0controll_app"
start "Node Service" cmd /k "%NODE_HOME%\node.exe" server.js
cd /d "%~dp0"
echo Node服务已启动 (端口3001)
timeout /t 3 /nobreak >nul

echo [3/3] 启动Nginx...
cd /d "%~dp0controll_app\nginx-1.16.1"
start /B nginx.exe
cd /d "%~dp0"
echo Nginx已启动 (端口80)
timeout /t 2 /nobreak >nul

echo.
echo ================================
echo  所有服务已启动完成
echo ================================
echo  前端访问: http://localhost
echo  后端API: http://localhost:8080
echo  辅助服务: http://localhost:3001
echo ================================
echo.
echo 按任意键打开浏览器访问系统...
pause >nul
start http://localhost
