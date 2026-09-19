@echo off
title 设备控制系统 - 后端服务

REM 检查Java环境
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到Java环境
    echo 请先安装Java 8或更高版本: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM 启动Spring Boot服务
echo 正在启动后端服务...
java -jar "%~dp0controll\target\controll-1.0-SNAPSHOT-jar-with-dependencies.jar"

REM 如果程序异常退出，暂停查看错误信息
if errorlevel 1 (
    echo.
    echo [错误] 程序异常退出
    pause
)
