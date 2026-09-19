# 将JAR包编译为Windows可执行文件

本项目已包含JAR包：`controll/target/controll-1.0-SNAPSHOT-jar-with-dependencies.jar`

## 方案1：使用批处理文件（推荐，最简单）

已提供 `start-backend.bat`，双击即可运行后端服务。

## 方案2：使用launch4j生成.exe文件

### 步骤：

1. **下载launch4j**
   - 官网：https://launch4j.sourceforge.net/
   - 直接下载：https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.zip

2. **解压launch4j**
   - 解压到任意目录，如 `C:\launch4j`

3. **生成exe文件**
   ```bash
   # 使用命令行版本（推荐）
   C:\launch4j\launch4jc.exe D:\code\total\controll_service\launch4j-config.xml
   
   # 或使用GUI版本
   # 打开 launch4j.exe，加载 launch4j-config.xml，点击"Build wrapper"
   ```

4. **生成结果**
   - 在 `controll_service` 目录会生成 `controll-service.exe`
   - 双击即可运行（仍需要系统安装Java 8+）

### 配置说明

`launch4j-config.xml` 已配置：
- 最低Java版本：1.8.0
- 64位JRE
- 控制台窗口模式
- 包含版本信息

## 方案3：使用jpackage（需要Java 14+）

如果升级到Java 14+，可以使用jpackage创建独立安装包：

```bash
jpackage --input controll/target \
  --name ControllService \
  --main-jar controll-1.0-SNAPSHOT-jar-with-dependencies.jar \
  --type exe \
  --win-console
```

这会生成包含JRE的完整安装包，用户无需安装Java。

## 推荐方案

- **开发/测试**：使用 `start-backend.bat`
- **分发给用户**：使用 launch4j 生成 `controll-service.exe`
- **企业部署**：考虑使用jpackage（需升级Java版本）
