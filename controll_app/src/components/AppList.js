import React from "react";
import axios, {get} from "axios";
import cors from 'cors'
import {Button, Divider, Tree, Row, Col, Checkbox} from "antd";
import {url} from "../Url";
import DeviceManagerBase from "./DeviceManager";
const TreeNode = Tree.TreeNode;


export default class AppList extends React.Component{

    constructor(props) {
        super(props);

        this.state = {
            apps:[],
            currentKey:"",
            move: true,
            devices:[]
        }
    }

    componentDidMount() {
        this.getApp();
        this.getDevice();

    }

    getDevice=()=>{
        axios.get(url+"/device/get_all_devices").then((res)=>{
            res.data.map((device)=>{
                device["checked"] = true
                return device
            })
            this.setState({devices:res.data})
        }).catch((error)=>{
            console.log(error)
        })
    }

    getApp=()=>{
        const url = "http://localhost/app/get_all_apps"
        axios.get(url).then((res)=>{
            this.setState({apps:res.data})
        }).catch((error)=>{
            console.log(error)
        })
    }




    openApp=(prop,currentKey)=>{
        this.setState({currentKey})
        axios.get( `http://localhost/app/open/${currentKey}`).then((res)=>{
            console.log("hhehehheh")

        }).catch((error)=>{
            console.log(error)
        })
    }


    getList=()=>{
        let {apps,currentKey} = this.state
        return apps.map((item,index)=>{
            let {appKey,appName} = item

            let type = currentKey == appKey ? "primary": "link"


            return <span key={`app_list_span_key_${appKey}_1`}>
                <span key={`app_list_span_key_${appKey}_2`}>
                    <Button key={appKey} type={type} value={appKey} onClick={(e) => this.openApp(e, appKey)}>
                       <span key={`app_list_span_key_${appKey}_3`} > {appName}</span>
                    </Button>
                </span>
                <Divider key={`app_list_span_key_${appKey}_4`} type="vertical"/>
            </span>


        })
    }

    disableKey=(e, deviceId)=>{
        let {devices} = this.state
        devices = devices.map(device=>{
            if(device['deviceId'] == deviceId){
                device['checked'] = e.target.checked
            }
            return device
        })
    }

    getDeviceComponent=()=>{
        let {devices,currentKey} = this.state
        return <div>
            {
                devices.map((item,index)=>{
                    let {deviceId,phoneNum} = item
                    return <div key={`getDeviceComponent${deviceId}_0`}>
                            <span key={`getDeviceComponent${deviceId}_1`}>
                            <span key={`getDeviceComponent${deviceId}_2`}>
                                <Checkbox  key={`getDeviceComponent${deviceId}_3`} defaultChecked={true} onChange={(e)=>this.disableKey(e,deviceId)}/> &nbsp;&nbsp;
                                {deviceId}
                            </span>

                            <Divider  key={`getDeviceComponent${deviceId}_4`} type="vertical"/>
                    </span>
                    </div>

                })
            }
        </div>
    }


    render() {



        return <div style={{margin: "10px"}} key={"app_list_data"}>

            <Row align={"center"}>

                <Button key={"casting"} type={"primary"}  onClick={(e)=>{
                    axios.get( `http://localhost/app/casting/all`).then((res)=>{}).catch((error)=>{})
                }}>
                    <span>投屏</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"stop-casting"} danger onClick={(e)=>{
                    axios.post( `http://localhost:3001/api/adb/stop_all_casting`).then((res)=>{
                        console.log('关闭投屏结果:', res.data);
                    }).catch((error)=>{
                        console.log('关闭投屏失败:', error);
                    })
                }}>
                    <span>关闭投屏</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"move"} type={"primary"}  onClick={(e)=>{
                    let deviceIds = this.state.devices.filter(item=>item.checked).map(device=>device.deviceId)
                    axios.post( `http://localhost/app/move/true/4/3`,deviceIds)
                        .then((res)=>{})
                        .catch((error)=>{})
                }}>
                    <span>滑动</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"move"} type={"primary"}  onClick={(e)=>{
                    let deviceIds = this.state.devices.filter(item=>item.checked).map(device=>device.deviceId)
                    axios.post( `http://localhost/app/mutilmove/true/4/3`,deviceIds).then((res)=>{}).catch((error)=>{})
                }}>
                    <span>多滑动</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"move"} type={"primary"}  onClick={(e)=>{
                    let deviceIds = this.state.devices.filter(item=>item.checked).map(device=>device.deviceId)
                    axios.post( `http://localhost/app/stop`).then((res)=>{
                    }).catch((error)=>{})
                }}>
                    <span>停止</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"polling"} type={"primary"}  onClick={(e)=>{
                    axios.get( `http://localhost/app/move/polling`).then((res)=>{
                    }).catch((error)=>{})
                }}>
                    <span>自动轮询</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"rewireless"} type={"primary"}  onClick={(e)=>{
                    axios.get( `http://localhost/device/rewireless`).then((res)=>{
                    }).catch((error)=>{})
                }}>
                    <span>全无线</span>
                </Button>
                <Divider type="vertical"/>
                <Button key={"rewireless"} type={"primary"}  onClick={(e)=>{
                    axios.get( `http://localhost/device/restart_adb`).then((res)=>{
                    }).catch((error)=>{})
                }}>
                    <span>重启ADB</span>
                </Button>
            </Row>
            <Divider type="horizontal"/>

            <Row>
                <Col span={4}>
                    {
                        this.getDeviceComponent()
                    }
                </Col>
                <Col span={16}>
                    {
                        this.getList()
                    }
                </Col>

            </Row>
        </div>
    }
}