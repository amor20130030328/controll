#!/bin/bash

echo "================================"
echo " 设备控制系统启动脚本"
echo "================================"
echo ""

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 设置Java环境
export JAVA_HOME="$SCRIPT_DIR/java/jre"
export PATH="$JAVA_HOME/bin:$PATH"

# 设置Node环境
export NODE_HOME="$SCRIPT_DIR/node"
export PATH="$NODE_HOME:$PATH"

# 检查Java是否可用
if ! command -v java &> /dev/null; then
    echo "[错误] Java环境配置失败"
    exit 1
fi

# 检查Node是否可用
if ! command -v node &> /dev/null; then
    echo "[错误] Node.js环境配置失败"
    exit 1
fi

echo "[1/3] 启动Spring Boot后端服务..."
cd "$SCRIPT_DIR/controll_service"
nohup java -jar "controll/target/controll-1.0-SNAPSHOT-jar-with-dependencies.jar" > backend.log 2>&1 &
BACKEND_PID=$!
echo "后端服务已启动 (PID: $BACKEND_PID, 端口8080)"
cd "$SCRIPT_DIR"
sleep 5

echo "[2/3] 启动Node.js辅助服务..."
cd "$SCRIPT_DIR/controll_app"
nohup node server.js > node.log 2>&1 &
NODE_PID=$!
echo "Node服务已启动 (PID: $NODE_PID, 端口3001)"
cd "$SCRIPT_DIR"
sleep 3

echo "[3/3] 启动Nginx..."
cd "$SCRIPT_DIR/controll_app/nginx-1.16.1"
./nginx
echo "Nginx已启动 (端口80)"
cd "$SCRIPT_DIR"

echo ""
echo "================================"
echo " 所有服务已启动完成"
echo "================================"
echo " 前端访问: http://localhost"
echo " 后端API: http://localhost:8080"
echo " 辅助服务: http://localhost:3001"
echo "================================"
echo ""
echo "提示: 使用 ./stop.sh 停止所有服务"
