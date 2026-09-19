# 自动化构建脚本说明

本项目提供了自动化构建和发布脚本，帮助你快速编译、打包并发布项目。

## 📦 脚本列表

| 脚本 | 功能 | 用途 |
|------|------|------|
| `build-all.bat` | **一键完成所有操作** | 自动执行后端、前端编译和打包发布 |
| `build-backend.bat` | 编译Java后端 | 生成 `controll-service.exe` |
| `build-frontend.bat` | 编译React前端 | 生成 `build/` 目录 |
| `build-and-publish.bat` | 打包并发布 | 创建final目录并上传GitHub |

## 🚀 快速开始

### 方式1：一键完成（推荐）

```bash
# 双击运行，或命令行执行
build-all.bat
```

这会自动完成：
1. ✅ 编译Java后端为EXE文件
2. ✅ 编译React前端为静态HTML
3. ✅ 复制所有文件到final目录
4. ✅ 生成压缩包final-release.zip
5. ✅ 提交并推送到GitHub

### 方式2：分步执行

```bash
# 1. 编译后端
build-backend.bat

# 2. 编译前端
build-frontend.bat

# 3. 打包发布
build-and-publish.bat
```

## 📋 前置要求

### 必需安装：
- ✅ **Java 8+** - 编译后端
- ✅ **Node.js** - 编译前端
- ✅ **Maven** - Java项目构建工具
- ✅ **Git** - 版本控制（可选，用于上传GitHub）

### 自动下载：
- ✅ **launch4j** - 自动下载用于生成EXE

## 📂 生成的文件结构

```
final/
├── backend/
│   ├── controll-service.exe          # 后端可执行文件
│   └── controll-1.0-SNAPSHOT.jar     # 原始JAR包
├── frontend/
│   ├── index.html                     # 前端入口
│   └── static/                        # 静态资源
├── tools/
│   ├── adb/                          # ADB工具
│   └── scrcpy/                       # 投屏工具
├── nginx/                            # Nginx服务器
├── 一键启动.bat                       # 启动脚本
├── README.md                         # 说明文档
└── ...

final-release.zip                     # 压缩包（上传到GitHub）
```

## 🎯 使用说明

### 开发者使用：

1. 修改代码后运行 `build-all.bat`
2. 自动编译、打包、上传

### 用户使用：

1. 从GitHub下载 `final-release.zip`
2. 解压
3. 双击 `一键启动.bat`
4. 访问 http://localhost

## ⚙️ 脚本详解

### build-backend.bat

**功能：** 编译Java后端并生成EXE文件

**步骤：**
1. 检查Maven环境
2. 执行 `mvn clean package`
3. 使用launch4j生成EXE
4. 输出到 `controll_service/controll-service.exe`

**输出：**
- `controll-service.exe` (61KB)
- `controll-1.0-SNAPSHOT.jar` (26MB)

---

### build-frontend.bat

**功能：** 编译React前端为静态HTML

**步骤：**
1. 检查Node.js和npm
2. 安装依赖（首次）
3. 执行 `npm run build`
4. 输出到 `controll_app/build/`

**输出：**
- `build/index.html`
- `build/static/` (JS、CSS、图片等)

---

### build-and-publish.bat

**功能：** 打包所有文件并上传到GitHub

**步骤：**
1. 清理并创建final目录
2. 复制后端EXE和JAR
3. 复制前端build文件
4. 复制tools和nginx
5. 创建一键启动脚本
6. 压缩为ZIP
7. Git提交并推送

**输出：**
- `final/` 目录
- `final-release.zip` (约100-150MB)

---

## 🔧 故障排除

### 问题1：找不到Maven

**解决方案：**
1. 下载Maven: https://maven.apache.org/download.cgi
2. 解压到C:\maven
3. 添加到系统PATH: `C:\maven\bin`

### 问题2：找不到Node.js

**解决方案：**
1. 下载Node.js: https://nodejs.org/
2. 安装（会自动添加到PATH）

### 问题3：npm install很慢

**解决方案：**
```bash
# 使用国内镜像
npm config set registry https://registry.npmmirror.com
```

### 问题4：launch4j下载失败

**解决方案：**
1. 手动下载: https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.zip
2. 解压到 `C:\launch4j`
3. 脚本会自动找到

### 问题5：Git推送失败

**解决方案：**
- 检查网络连接
- 确认SSH密钥配置
- 手动执行：`git push`

## 📝 自定义配置

### 修改输出目录

编辑 `build-and-publish.bat`：
```batch
set FINAL_DIR=%~dp0release  REM 改为你想要的目录名
```

### 跳过GitHub上传

注释掉 `build-and-publish.bat` 中的Git部分（第[5/5]步骤）

### 修改EXE配置

编辑 `controll_service/launch4j-config.xml`

## 🎓 最佳实践

1. **首次使用**：先单独运行 `build-backend.bat` 和 `build-frontend.bat` 确保环境正常
2. **日常开发**：使用 `build-all.bat` 一键完成
3. **发布版本**：确保测试通过后再运行
4. **版本标记**：在Git中打tag标记重要版本

## 📚 相关文档

- [launch4j配置说明](controll_service/BUILD_EXE.md)
- [项目README](README.md)
- [GitHub仓库](https://github.com/amor20130030328/controll)

---

**作者**: amor20130030328  
**最后更新**: 2026-09-20
