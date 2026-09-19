import React from "react";
import { Button, Card, Space, message, Badge } from 'antd';
import { PlayCircleOutlined, StopOutlined, SyncOutlined } from '@ant-design/icons';
import axios from 'axios';

export default class NginxManager extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isRunning: false,
            loading: false,
            checking: false
        };
    }

    componentDidMount() {
        this.checkNginxStatus();
    }

    checkNginxStatus = async () => {
        this.setState({ checking: true });
        try {
            const response = await axios.get('http://localhost:3001/api/nginx/status');
            this.setState({
                isRunning: response.data.running,
                checking: false
            });
        } catch (error) {
            message.error('检查Nginx状态失败');
            this.setState({ checking: false });
        }
    }

    startNginx = async () => {
        this.setState({ loading: true });
        try {
            const response = await axios.post('http://localhost:3001/api/nginx/start');
            if (response.data.success) {
                message.success(response.data.message);
                this.setState({ isRunning: true });
            } else {
                message.warning(response.data.message);
            }
        } catch (error) {
            message.error('启动Nginx失败');
        } finally {
            this.setState({ loading: false });
            this.checkNginxStatus();
        }
    }

    stopNginx = async () => {
        this.setState({ loading: true });
        try {
            const response = await axios.post('http://localhost:3001/api/nginx/stop');
            if (response.data.success) {
                message.success(response.data.message);
                this.setState({ isRunning: false });
            } else {
                message.error(response.data.message);
            }
        } catch (error) {
            message.error('停止Nginx失败');
        } finally {
            this.setState({ loading: false });
            this.checkNginxStatus();
        }
    }

    render() {
        const { isRunning, loading, checking } = this.state;

        return (
            <div style={{ padding: '20px' }}>
                <Card
                    title="Nginx服务管理"
                    style={{ maxWidth: 600, margin: '0 auto' }}
                >
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        <div style={{ textAlign: 'center' }}>
                            <p>当前状态: {' '}
                                <Badge
                                    status={isRunning ? "processing" : "default"}
                                    text={isRunning ? "运行中" : "未运行"}
                                />
                            </p>
                        </div>

                        <Space size="middle" style={{ width: '100%', justifyContent: 'center' }}>
                            <Button
                                type="primary"
                                icon={<PlayCircleOutlined />}
                                onClick={this.startNginx}
                                loading={loading}
                                disabled={isRunning}
                            >
                                启动Nginx
                            </Button>

                            <Button
                                danger
                                icon={<StopOutlined />}
                                onClick={this.stopNginx}
                                loading={loading}
                                disabled={!isRunning}
                            >
                                停止Nginx
                            </Button>

                            <Button
                                icon={<SyncOutlined />}
                                onClick={this.checkNginxStatus}
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
