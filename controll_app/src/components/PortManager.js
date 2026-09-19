import React from "react";
import { Button, Card, Space, message, Badge, Typography, Alert } from 'antd';
import { DeleteOutlined, SyncOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Text } = Typography;

export default class PortManager extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            occupied: false,
            processes: [],
            checking: false,
            killing: false,
            port: 8080,
            lastError: null
        };
    }

    componentDidMount() {
        this.checkPortStatus();
    }

    checkPortStatus = async () => {
        this.setState({ checking: true, lastError: null });
        try {
            const response = await axios.get(`http://localhost:3001/api/port/status?port=${this.state.port}`);
            this.setState({
                occupied: response.data.occupied,
                processes: response.data.processes || [],
                checking: false
            });
        } catch (error) {
            message.error('检查端口状态失败');
            this.setState({ checking: false });
        }
    }

    killPort = async () => {
        this.setState({ killing: true, lastError: null });
        try {
            const response = await axios.post('http://localhost:3001/api/port/kill', { port: this.state.port });
            if (response.data.success) {
                message.success(response.data.message);
                this.setState({ lastError: null });
            } else {
                this.setState({ lastError: response.data.message });
                message.error(response.data.message);
            }
            this.checkPortStatus();
        } catch (error) {
            message.error('终止进程失败');
        } finally {
            this.setState({ killing: false });
        }
    }

    render() {
        const { occupied, processes, checking, killing, port, lastError } = this.state;

        return (
            <div style={{ padding: '20px' }}>
                <Card
                    title={`端口 ${port} 进程管理`}
                    style={{ maxWidth: 600, margin: '0 auto' }}
                >
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        {lastError && lastError.includes('管理员') && (
                            <Alert
                                message="权限不足"
                                description={
                                    <div>
                                        <p>无法终止该进程，请以管理员身份运行服务器：</p>
                                        <Text code>以管理员身份打开命令提示符，然后运行：node server.js</Text>
                                    </div>
                                }
                                type="warning"
                                showIcon
                            />
                        )}
                        <div style={{ textAlign: 'center' }}>
                            <p>端口 {port} 状态:{' '}
                                <Badge
                                    status={occupied ? "error" : "success"}
                                    text={occupied ? "已被占用" : "空闲"}
                                />
                            </p>
                            {occupied && processes.length > 0 && (
                                <p>
                                    占用进程 PID：{' '}
                                    {processes.map(pid => (
                                        <Text key={pid} code style={{ marginRight: 4 }}>{pid}</Text>
                                    ))}
                                </p>
                            )}
                        </div>

                        <Space size="middle" style={{ width: '100%', justifyContent: 'center' }}>
                            <Button
                                danger
                                icon={<DeleteOutlined />}
                                onClick={this.killPort}
                                loading={killing}
                                disabled={!occupied}
                            >
                                终止占用进程
                            </Button>

                            <Button
                                icon={<SyncOutlined />}
                                onClick={this.checkPortStatus}
                                loading={checking}
                            >
                                刷新状态
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </div>
        );
    }
}
