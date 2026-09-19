import React from 'react'
import { Flex, Layout,Menu } from 'antd';
import AppContent from "./components/AppContent";
import {MenuFoldOutlined} from "@ant-design/icons";
const { Header, Footer, Sider, Content } = Layout;
import './App.css';
import axios from 'axios';

const headerStyle: React.CSSProperties = {
    textAlign: 'center',
    color: '#fff',
    height: 64,
    paddingInline: 48,
    lineHeight: '64px',
    backgroundColor: '#4096ff',
};

const contentStyle: React.CSSProperties = {
    textAlign: 'center',
    minHeight: 120,
    lineHeight: '120px',
    color: '#fff',
    backgroundColor: '#0958d9',
};

const siderStyle: React.CSSProperties = {
    textAlign: 'center',
    lineHeight: '900px',
    color: '#fff',
    backgroundColor: '#1677ff',
};

const footerStyle: React.CSSProperties = {
    textAlign: 'center',
    color: '#fff',
    backgroundColor: '#4096ff',
};

const layoutStyle = {
    borderRadius: 8,
    overflow: 'hidden',
    width: 'calc(100% - 8px)',
    height: 'calc(100% - 8px)',
    maxHeight: 'calc(100% - 8px)',
    maxWidth: 'calc(100% - 8px)',
};

export default class ControllApp extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            menu: 'device_manager'
        };
    }

    changeMenu = (props)=>{
        let menu = props['key']
        console.log("props")
        this.setState({menu})
        console.log(menu)
    }




    render() {

        const items = [
            { key: 'device_manager', label: 'app管理' },
            { key: 'app_list', label: '群控app管理' },
            { key: 'nginx_manager', label: 'Nginx管理' },
            { key: 'port_manager', label: '端口管理' },
            { key: 'network_scanner', label: '网络IP扫描' },

        ];

        const windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;

        // 获取窗口高度
        const windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;


        return <div>
            <div className="App">
                <Flex gap="middle" wrap>
                    <Layout  style={{height:windowHeight}}>>
                        <Sider width="20%" style={siderStyle}>
                            <Menu
                                defaultSelectedKeys={['1']}
                                defaultOpenKeys={['sub1']}
                                mode="inline"
                                theme="dark"
                                onSelect={(item)=>{
                                    let menu = item['key']
                                    this.setState({menu}
                                )}}
                                inlineCollapsed={<MenuFoldOutlined/>}
                                items={items}
                            />

                        </Sider>
                        <Layout>
                            <Header style={headerStyle}>Header</Header>
                            <Content>
                                <AppContent menu={this.state.menu}/>
                            </Content>
                            <Footer style={footerStyle}>Footer</Footer>
                        </Layout>
                    </Layout>
                </Flex>
            </div>
        </div>
    }
}