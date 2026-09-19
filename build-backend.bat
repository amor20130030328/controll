@echo off
chcp 65001 >nul
echo ================================
echo  编译Java后端为EXE文件
echo ================================
echo.

REM 切换到后端目录
cd /d "%~dp0controll_service"

REM 检查Maven是否安装
where mvn >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到Maven，请先安装Maven
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo [1/3] 清理旧的构建文件...
call mvn clean

echo.
echo [2/3] 编译Java项目并打包JAR...
call mvn package -DskipTests
if errorlevel 1 (
    echo [错误] Maven编译失败
    pause
    exit /b 1
)

echo.
echo [3/3] 生成EXE文件...

REM 检查launch4j是否存在
set LAUNCH4J=C:\launch4j\launch4jc.exe
if not exist "%LAUNCH4J%" (
    set LAUNCH4J=%TEMP%\launch4j\launch4jc.exe
)

if not exist "%LAUNCH4J%" (
    echo [警告] 未找到launch4j，正在下载...

    REM 下载launch4j
    powershell -Command "& {Invoke-WebRequest -Uri 'https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.zip/download' -OutFile '%TEMP%\launch4j.zip'}"

    REM 解压
    powershell -Command "& {Expand-Archive -Path '%TEMP%\launch4j.zip' -DestinationPath '%TEMP%' -Force}"

    set LAUNCH4J=%TEMP%\launch4j\launch4jc.exe
)

REM 使用launch4j生成exe
"%LAUNCH4J%" "%~dp0controll_service\launch4j-config.xml"
if errorlevel 1 (
    echo [错误] 生成EXE失败
    pause
    exit /b 1
)

echo.
echo ================================
echo  编译完成！
echo ================================
echo EXE文件位置: %~dp0controll_service\controll-service.exe
echo JAR文件位置: %~dp0controll_service\controll\target\controll-1.0-SNAPSHOT.jar
echo.
pause
