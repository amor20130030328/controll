@echo off
chcp 65001 >nul
echo ================================
echo  正在停止所有服务...
echo ================================
echo.

echo [1/3] 停止Nginx...
taskkill /F /IM nginx.exe /T >nul 2>&1
if errorlevel 1 (
    echo Nginx未运行或已停止
) else (
    echo Nginx已停止
)

echo [2/3] 停止Node.js服务 (端口3001)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":3001" ^| find "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
    if not errorlevel 1 echo Node服务已停止 (PID: %%a)
)

echo [3/3] 停止Java后端服务 (端口8080)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
    if not errorlevel 1 echo 后端服务已停止 (PID: %%a)
)

echo.
echo ================================
echo  所有服务已停止
echo ================================
pause
