@echo off
chcp 65001 >nul
echo ================================
echo  构建并发布最终版本
echo ================================
echo.

set FINAL_DIR=%~dp0final
set BACKEND_EXE=%~dp0controll_service\controll-service.exe
set FRONTEND_BUILD=%~dp0controll_app\build
set TOOLS_DIR=%~dp0tools
set NGINX_DIR=%~dp0controll_app\nginx-1.16.1

echo [1/5] 清理final目录...
if exist "%FINAL_DIR%" (
    rmdir /s /q "%FINAL_DIR%"
)
mkdir "%FINAL_DIR%"

echo.
echo [2/5] 检查后端EXE文件...
if not exist "%BACKEND_EXE%" (
    echo [错误] 未找到后端EXE文件，请先运行 build-backend.bat
    pause
    exit /b 1
)

echo.
echo [3/5] 检查前端构建文件...
if not exist "%FRONTEND_BUILD%\index.html" (
    echo [错误] 未找到前端构建文件，请先运行 build-frontend.bat
    pause
    exit /b 1
)

echo.
echo [4/5] 复制文件到final目录...

REM 创建目录结构
mkdir "%FINAL_DIR%\backend"
mkdir "%FINAL_DIR%\frontend"
mkdir "%FINAL_DIR%\tools"
mkdir "%FINAL_DIR%\nginx"

REM 复制后端exe
echo   - 复制后端EXE...
copy "%BACKEND_EXE%" "%FINAL_DIR%\backend\" >nul
copy "%~dp0controll_service\controll\target\controll-1.0-SNAPSHOT.jar" "%FINAL_DIR%\backend\" >nul

REM 复制前端构建文件
echo   - 复制前端文件...
xcopy "%FRONTEND_BUILD%\*" "%FINAL_DIR%\frontend\" /E /I /Q >nul

REM 复制工具
echo   - 复制ADB和scrcpy工具...
xcopy "%TOOLS_DIR%\*" "%FINAL_DIR%\tools\" /E /I /Q >nul

REM 复制Nginx
echo   - 复制Nginx...
xcopy "%NGINX_DIR%\*" "%FINAL_DIR%\nginx\" /E /I /Q >nul

REM 复制启动脚本和文档
echo   - 复制启动脚本和文档...
copy "%~dp0start.bat" "%FINAL_DIR%\" >nul
copy "%~dp0stop.bat" "%FINAL_DIR%\" >nul
copy "%~dp0README.md" "%FINAL_DIR%\" >nul

REM 创建简化的启动脚本
echo   - 创建启动脚本...
(
echo @echo off
echo chcp 65001 ^>nul
echo echo 正在启动设备控制系统...
echo echo.
echo.
echo REM 启动后端
echo start "后端服务" cmd /k "cd backend && controll-service.exe"
echo timeout /t 5 /nobreak ^>nul
echo.
echo REM 启动Nginx
echo cd nginx
echo start /B nginx.exe
echo cd ..
echo.
echo echo 系统已启动！
echo echo 访问地址: http://localhost
echo echo.
echo start http://localhost
echo pause
) > "%FINAL_DIR%\一键启动.bat"

echo.
echo [5/5] 提交到GitHub...

REM 检查Git
where git >nul 2>&1
if errorlevel 1 (
    echo [警告] 未找到Git，跳过提交
    goto :skip_git
)

REM 添加final目录到.gitignore（避免冲突）
findstr /C:"final/" "%~dp0.gitignore" >nul 2>&1
if errorlevel 1 (
    echo final/ >> "%~dp0.gitignore"
)

echo   - 检查文件大小...
powershell -Command "& {$size = (Get-ChildItem '%FINAL_DIR%' -Recurse | Measure-Object -Property Length -Sum).Sum / 1MB; Write-Host ('Final目录大小: {0:N2} MB' -f $size)}"

echo.
echo   - 压缩为ZIP文件...
powershell -Command "& {Compress-Archive -Path '%FINAL_DIR%\*' -DestinationPath '%~dp0final-release.zip' -Force}"

if exist "%~dp0final-release.zip" (
    echo   - 添加到Git...
    git add final-release.zip .gitignore
    git commit -m "release: 添加最终发布包 (含前后端编译文件)"

    echo   - 推送到GitHub...
    git push

    if errorlevel 0 (
        echo.
        echo ================================
        echo  发布成功！
        echo ================================
        echo 本地文件: %FINAL_DIR%
        echo 压缩包: %~dp0final-release.zip
        echo GitHub: https://github.com/amor20130030328/controll
    ) else (
        echo [警告] Git推送失败
    )
) else (
    echo [错误] 压缩失败
)

:skip_git

echo.
echo ================================
echo  构建完成！
echo ================================
echo.
echo 最终文件位置: %FINAL_DIR%
echo.
echo 目录结构:
echo   final\
echo   ├── backend\
echo   │   ├── controll-service.exe
echo   │   └── controll-1.0-SNAPSHOT.jar
echo   ├── frontend\
echo   │   └── index.html + 静态资源
echo   ├── tools\
echo   │   ├── adb\
echo   │   └── scrcpy\
echo   ├── nginx\
echo   ├── 一键启动.bat
echo   ├── README.md
echo   └── ...
echo.
echo 使用方法: 双击 final\一键启动.bat
echo.
pause
