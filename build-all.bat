@echo off
chcp 65001 >nul
echo ================================
echo  自动构建和发布系统
echo ================================
echo.
echo 本脚本将依次执行以下操作：
echo 1. 编译Java后端为EXE文件
echo 2. 编译前端React项目
echo 3. 打包到final目录并上传到GitHub
echo.
echo 按任意键开始，或Ctrl+C取消...
pause >nul

echo.
echo ================================
echo  步骤 1/3: 编译Java后端
echo ================================
call "%~dp0build-backend.bat"
if errorlevel 1 (
    echo [错误] 后端编译失败
    pause
    exit /b 1
)

echo.
echo ================================
echo  步骤 2/3: 编译前端
echo ================================
call "%~dp0build-frontend.bat"
if errorlevel 1 (
    echo [错误] 前端编译失败
    pause
    exit /b 1
)

echo.
echo ================================
echo  步骤 3/3: 打包并发布
echo ================================
call "%~dp0build-and-publish.bat"

echo.
echo ================================
echo  全部完成！
echo ================================
pause
