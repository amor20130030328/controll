import React from "react";
import { Button, Card, Table, Badge, Tag, Typography, Space } from 'antd';
import { SyncOutlined, WifiOutlined, MobileOutlined, LaptopOutlined, QuestionOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Text } = Typography;

function DeviceTypeTag({ deviceType }) {
    if (!deviceType || deviceType === '未知') return <Tag color="default">未知</Tag>;
    if (deviceType.includes('手机') || deviceType.includes('平板')) {
        return <Tag color="cyan" icon={<MobileOutlined />}>{deviceType}</Tag>;
    }
    if (deviceType.includes('电脑') || deviceType.includes('笔记本')) {
        return <Tag color="blue" icon={<LaptopOutlined />}>{deviceType}</Tag>;
    }
    if (deviceType.includes('路由') || deviceType.includes('网关')) {
        return <Tag color="orange">{deviceType}</Tag>;
    }
    return <Tag color="geekblue">{deviceType}</Tag>;
}

function OSTag({ os }) {
    if (!os || os === '未知') return <Tag color="default">未知</Tag>;
    if (os === 'iOS') return <Tag color="black">iOS</Tag>;
    if (os === 'Android') return <Tag color="green">Android</Tag>;
    if (os === 'Windows') return <Tag color="blue">Windows</Tag>;
    return <Tag>{os}</Tag>;
}

export default class NetworkScanner extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            hosts: [],
            localIPs: [],
            subnet: '',
            scanning: false,
            scanned: false,
        };
    }

    componentDidMount() {
        this.scan();
    }

    scan = async () => {
        this.setState({ scanning: true });
        try {
            const response = await axios.get('http://localhost:3001/api/network/scan');
            if (response.data.success) {
                this.setState({
                    hosts: response.data.hosts,
                    localIPs: response.data.localIPs,
                    subnet: response.data.subnet,
                    scanned: true,
                });
            }
        } catch (error) {
            console.error('扫描失败', error);
        } finally {
            this.setState({ scanning: false });
        }
    }

    render() {
        const { hosts, subnet, scanning, scanned, localIPs } = this.state;

        const columns = [
            {
                title: 'IP 地址',
                dataIndex: 'ip',
                key: 'ip',
                width: 160,
                render: (ip, record) => (
                    <span>
                        <Text strong>{ip}</Text>
                        {record.isLocal && <Tag color="blue" style={{ marginLeft: 6 }}>本机</Tag>}
                    </span>
                )
            },
            {
                title: 'MAC 地址',
                dataIndex: 'mac',
                key: 'mac',
                width: 160,
                render: mac => mac ? <Text code style={{ fontSize: 12 }}>{mac}</Text> : <Text type="secondary">-</Text>
            },
            {
                title: '厂商',
                dataIndex: 'vendor',
                key: 'vendor',
                width: 120,
                render: vendor => vendor && vendor !== '未知' ? <Text>{vendor}</Text> : <Text type="secondary">-</Text>
            },
            {
                title: '设备类型',
                dataIndex: 'deviceType',
                key: 'deviceType',
                width: 130,
                render: deviceType => <DeviceTypeTag deviceType={deviceType} />
            },
            {
                title: '系统',
                dataIndex: 'os',
                key: 'os',
                width: 100,
                render: os => <OSTag os={os} />
            },
            {
                title: '状态',
                key: 'status',
                width: 80,
                render: () => <Badge status="success" text="在线" />
            }
        ];

        return (
            <div style={{ padding: '20px' }}>
                <Card
                    title={
                        <Space>
                            <WifiOutlined />
                            <span>局域网 IP 扫描</span>
                            {subnet && <Tag color="purple">网段: {subnet}.x</Tag>}
                        </Space>
                    }
                    extra={
                        <Button
                            type="primary"
                            icon={<SyncOutlined spin={scanning} />}
                            onClick={this.scan}
                            loading={scanning}
                        >
                            {scanning ? '扫描中...' : '重新扫描'}
                        </Button>
                    }
                >
                    {scanned && (
                        <p style={{ marginBottom: 12 }}>
                            共发现 <Text strong>{hosts.length}</Text> 个在线设备
                            {localIPs.length > 0 && (
                                <>，本机IP：{localIPs.map(ip => <Tag key={ip} color="blue">{ip}</Tag>)}</>
                            )}
                        </p>
                    )}
                    <Table
                        columns={columns}
                        dataSource={hosts.map((h, i) => ({ ...h, key: i }))}
                        loading={scanning}
                        size="small"
                        pagination={false}
                        scroll={{ x: 800 }}
                        locale={{ emptyText: scanned ? '未发现在线设备' : '点击扫描按钮开始' }}
                    />
                </Card>
            </div>
        );
    }
}
