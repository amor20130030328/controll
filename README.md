# 设备控制系统 (Android Device Control System)

一个功能强大的Android设备集群管理平台，支持设备远程控制、投屏、应用管理、网络扫描等功能。

## 📋 项目简介

本系统是一个完整的全栈应用，用于管理和控制多台Android设备。适用于自动化测试、设备农场、远程设备管理等场景。

### 核心功能

- **设备管理**: 远程连接、管理多台Android设备
- **投屏功能**: 基于scrcpy的高性能设备投屏
- **应用控制**: 安装、卸载、启动应用，支持分屏显示
- **灯光调节**: 远程控制设备屏幕亮度
- **网络扫描**: 局域网设备发现，自动识别设备类型（Apple/小米/华为/OPPO/vivo等）
- **Nginx管理**: Web服务器启停控制
- **端口管理**: 查看和终止占用端口的进程

## 🎯 技术栈

### 前端
- React 18
- Ant Design 5
- Axios

### 后端
- Spring Boot 2.3.4
- MyBatis
- MySQL 8.0

### 辅助服务
- Node.js (Express)
- Nginx 1.16.1

### 工具
- ADB (Android Debug Bridge)
- scrcpy (投屏工具)

## 📦 系统要求

- **操作系统**: Windows 10+ / Linux
- **内存**: 建议4GB以上
- **磁盘空间**: 约600MB
- **网络**: 需要访问远程MySQL数据库

**注意**: 本项目已包含Java和Node.js运行环境，无需单独安装。

## 🚀 快速开始

### Windows用户

1. **克隆仓库**
   ```bash
   git clone https://github.com/amor20130030328/controll.git
   cd controll
   ```

2. **启动系统**
   
   双击运行 `start.bat` 文件，系统会自动启动所有服务。

3. **访问系统**
   
   浏览器自动打开 http://localhost

### Linux用户

1. **克隆仓库**
   ```bash
   git clone https://github.com/amor20130030328/controll.git
   cd controll
   ```

2. **添加执行权限**
   ```bash
   chmod +x start.sh
   ```

3. **启动系统**
   ```bash
   ./start.sh
   ```

4. **访问系统**
   
   打开浏览器访问 http://localhost

## 🔌 端口说明

| 端口 | 服务 | 说明 |
|------|------|------|
| 80 | Nginx | 前端入口，反向代理 |
| 8080 | Spring Boot | 后端API服务 |
| 3001 | Node.js | 辅助服务（设备管理、系统命令） |

## 📁 目录结构

```
controll/
├── controll_app/              # React前端应用
│   ├── build/                 # 构建产物
│   ├── server.js             # Node.js辅助服务
│   ├── nginx-1.16.1/         # Nginx服务器
│   └── ...
├── controll_service/          # Spring Boot后端
│   └── controll/
│       └── target/           # JAR包
├── java/                      # JRE 8运行环境
├── node/                      # Node.js运行环境
├── tools/                     # 工具目录
│   ├── adb/                  # ADB工具
│   └── scrcpy/               # Scrcpy投屏工具
├── start.bat                  # Windows启动脚本
├── stop.bat                   # Windows停止脚本
├── start.sh                   # Linux启动脚本
└── README.md                  # 本文件
```

## 🛠️ 使用说明

### 连接Android设备

1. 确保Android设备开启USB调试或网络ADB
2. 在系统的"网络扫描"功能中扫描局域网设备
3. 点击"连接"按钮建立ADB连接

### 投屏

1. 连接设备后，在设备列表中点击"投屏"按钮
2. 系统会打开scrcpy窗口显示设备屏幕
3. 可以直接在投屏窗口中操作设备

### 应用管理

1. 选择已连接的设备
2. 查看设备上已安装的应用列表
3. 支持启动应用、分屏显示等功能

## 🛑 停止服务

### Windows
双击运行 `stop.bat` 文件

### Linux
```bash
./stop.sh
```

## ⚠️ 常见问题

### 1. 启动失败

**问题**: 双击start.bat无反应或闪退
**解决方案**:
- 检查80、8080、3001端口是否被占用
- 以管理员身份运行start.bat
- 查看日志文件排查问题

### 2. 无法访问页面

**问题**: 浏览器无法打开 http://localhost
**解决方案**:
- 确认Nginx已启动（任务管理器中查找nginx.exe进程）
- 检查防火墙是否拦截80端口
- 尝试访问 http://localhost:8080 测试后端是否正常

### 3. 设备连接失败

**问题**: ADB连接设备失败
**解决方案**:
- 确认设备已开启USB调试或网络ADB
- 检查设备和电脑在同一局域网
- 手动执行 `tools\adb\adb.exe devices` 测试ADB是否正常

### 4. 数据库连接失败

**问题**: 后端服务启动报数据库连接错误
**解决方案**:
- 确认网络可访问远程MySQL服务器 (101.200.46.244:3306)
- 检查数据库账户权限
- 如需修改数据库配置，编辑 `controll_service/controll/src/main/resources/application.properties`

### 5. 端口被占用

**问题**: 启动时提示端口已被占用
**解决方案**:
- 使用系统自带的"端口管理"功能查看并终止占用进程
- 或手动查找并关闭占用端口的程序

## 🔐 安全说明

**重要**: 本项目的数据库配置文件中包含远程数据库的访问凭据。如果您需要在生产环境使用，请务必：

1. 修改数据库密码
2. 使用环境变量或配置文件管理敏感信息
3. 配置防火墙规则限制访问

## 📝 开发说明

### 重新构建前端

```bash
cd controll_app
npm install
npm run build
```

### 重新构建后端

```bash
cd controll_service
mvn clean package
```

## 📄 许可证

本项目仅供学习和研究使用。

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📧 联系方式

如有问题或建议，请通过GitHub Issues联系。

---

**开发者**: amor20130030328  
**仓库地址**: https://github.com/amor20130030328/controll
