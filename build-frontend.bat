@echo off
chcp 65001 >nul
echo ================================
echo  编译前端代码
echo ================================
echo.

REM 切换到前端目录
cd /d "%~dp0controll_app"

REM 检查Node.js是否安装
where node >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到Node.js，请先安装Node.js
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

REM 检查npm是否安装
where npm >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到npm
    pause
    exit /b 1
)

echo [1/3] 检查依赖...
if not exist "node_modules" (
    echo 正在安装依赖（首次编译需要较长时间）...
    call npm install
    if errorlevel 1 (
        echo [错误] npm install失败
        pause
        exit /b 1
    )
) else (
    echo 依赖已存在，跳过安装
)

echo.
echo [2/3] 清理旧的构建文件...
if exist "build" (
    rmdir /s /q build
)

echo.
echo [3/3] 编译React项目...
call npm run build
if errorlevel 1 (
    echo [错误] 编译失败
    pause
    exit /b 1
)

echo.
echo ================================
echo  编译完成！
echo ================================
echo 构建文件位置: %~dp0controll_app\build\
echo 入口文件: %~dp0controll_app\build\index.html
echo.
pause
