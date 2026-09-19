import React from "react";
import axios from "axios";
import cors from 'cors'
import {Button, Divider, Table, Form, Input, Radio, Modal} from "antd";
import { EditFilled } from '@ant-design/icons';
const FormItem = Form.Item;
import {url} from '../Url'
import AppList from "./AppList";


export default class DeviceManagerBase extends React.Component{

    constructor(props) {
        super(props);

        this.state = {
            devices:[],
            visible:false,
            current_device : {}
        }
    }

    componentDidMount() {
        this.queryDeviceList();
    }


    queryDeviceList=()=>{
        axios.get(url+"/device/get_all_devices").then((res)=>{
            this.setState({devices:res.data})
        }).catch((error)=>{
            console.log(error)
        })
    }

    showModal = () => {
        this.setState({
            visible: true,
        });
    }

    getWindow=(prop,deviceId)=>{

        axios.get( `http://localhost/local_device/getWindow/${deviceId}`).then((res)=>{
            console.log(11111)
        }).catch((error)=>{

        })
    }

    init=(prop,deviceId)=>{

        axios.get( `http://localhost/local_device/init/${deviceId}`).then((res)=>{
            console.log(11111)
        }).catch((error)=>{
        })
    }

    installApp=(prop,deviceId)=>{

        axios.get( `http://localhost/local_device/installApp/${deviceId}`).then((res)=>{
            console.log(11111)
        }).catch((error)=>{
        })
    }

    casting=(prop,item)=>{

        axios.post( `http://localhost/device/casting`,item).then((res)=>{
        }).catch((error)=>{
        })
    }

    stopCasting=(e,deviceId)=>{
        axios.post(`http://localhost/api/adb/stop-casting`, { deviceId }).then((res)=>{
            console.log('关屏成功', res.data);
        }).catch((error)=>{
            console.log('关屏失败', error);
        })
    }

    AlwaysOnLight=(e,deviceId)=>{
        axios.get(`http://localhost/device/always_on_light/${deviceId}`).then((res)=>{
            console.log('关屏成功', res.data);
        }).catch((error)=>{
            console.log('关屏失败', error);
        })
    }


    addDevice=(data)=>{

        axios.post( `http://localhost/device/put`,data).then((res)=>{
            console.log(data)
            this.queryDeviceList();
            this.setState({visible:false})
        }).catch((error)=>{
            console.log("新增失败");
        })
    }

    controllLight=(e,deviceId,isUp)=>{
        axios.get( `${url}/device/light/${deviceId}/${isUp}`).then((res)=>{

        }).catch((error)=>{
            console.log("调低暗度");
        })
    }

    switchWireless=(e,deviceId,isUp)=>{
        axios.get( `${url}/device/wireless/${deviceId}`).then((res)=>{

        }).catch((error)=>{
            console.log("switchWireless");
        })
    }

    takenOn=(e, deviceId)=>{
        axios.get( `${url}/device/cmdtype/take_on/${deviceId}`).then((res)=>{

        }).catch((error)=>{
            console.log("switchWireless");
        })
    }

    reduceAudio=(e, deviceId)=>{
        axios.get( `${url}/device/cmdtype/reduc_audio/${deviceId}`).then((res)=>{

        }).catch((error)=>{
            console.log("switchWireless");
        })
    }


    getList=()=>{
        let {devices} = this.state
        return devices.map((item,index)=>{
            let {key,name} = item

            return <span key={`app_list_span_key_${key}`}>
                <span key={item} type="primary" >
                    {item}
                    <Button type={"primary"} onClick={(e)=>this.installApp(e,item)}>安装</Button><Divider type="vertical"/>
                </span> <Divider type="vertical"/>
            </span>


        })
    }

    render() {


        const columns = [
            {
                title: '设别号',
                dataIndex: 'deviceId',
                align:"center"
            },
            {
                title: '姓名',
                dataIndex: 'userName',
                align:"center"
            },
            {
                title: '手机号',
                dataIndex: 'phoneNum',
                align:"center"
            },
            {
                title: '身份证号码',
                dataIndex: 'idCard',
                align:"center"
            },
            {
                title: '操作',
                dataIndex: 'operation',
                align:"center",
                width: 600
            }

        ];

        const data = this.state.devices.map((item,index)=>{
            return {
                key: index,
                deviceId: <span  style={{color:"blue"}} >{item.deviceId} <EditFilled onClick={()=>{this.setState({visible:true, current_device:item})}}/></span>,
                userName : <span>{item.userName}</span>,
                phoneNum : <span>{item.phoneNum}</span>,
                idCard : <span>{item.idCard}</span>,
                operation: <div style={{display:'flex', flexWrap:'nowrap', gap:'2px', padding:'1px 0', overflowX:'auto'}}>
                    <Button size="small" type={"primary"} onClick={(e)=>this.init(e,item.deviceId)}>初始化</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.getWindow(e,item.deviceId)}>分屏</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.installApp(e,item.deviceId)}>安装APP</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.casting(e,item)}>投屏</Button>
                    <Button size="small" danger onClick={(e)=>this.stopCasting(e,item.deviceId)}>关屏</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.AlwaysOnLight(e,item.deviceId)}>长亮</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.controllLight(e,item.deviceId,false)}>调低亮度</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.controllLight(e,item.deviceId,true)}>调高亮度</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.takenOn(e,item.deviceId)}>电源</Button>
                    <Button size="small" type={"primary"} onClick={(e)=>this.reduceAudio(e,item.deviceId)}>音量</Button>
                    {item.deviceId.includes(".") ? null :
                        <Button size="small" type="primary" onClick={(e) => this.switchWireless(e, item.deviceId)}>切无线</Button>
                    }
                </div>
            }
        })


        let rowSelection = false;

        //const [form] = Form.useForm();

        return <div>
            <Modal
                title="手机信息"
                visible={this.state.visible}
                onOk={()=>{this.setState({visible:false})}}
                onCancel={()=>{this.setState({visible:false})}}
            >
                <Form
                    name="新增手机基础信息"
                    initialValues={{ remember: true }}
                    onFinish={(item)=>this.addDevice(item)}
                    onFinishFailed={()=>{}}
                >
                    <Form.Item
                        label="设备Id"
                        name="deviceId"
                        initialValue={this.state.current_device.deviceId}
                    >
                        <Input  disabled={true}/>

                    </Form.Item>

                    <Form.Item
                        label="姓名"
                        name="userName"
                        rules={[{ message: '姓名:' }]}
                        initialValue={this.state.current_device.username}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="手机号码"
                        name="phoneNum"
                        rules={[{ message: '手机号码:' }]}
                        initialValue={this.state.current_device.phoneNum}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="身份证号码"
                        name="idCard"
                        rules={[{ message: '身份证号码!' }]}
                        initialValue={this.state.current_device.idCard}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit">
                            提交
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
            <Table rowSelection={rowSelection} columns={columns} dataSource={data} scroll={{ y: 300 }} size="small" rowClassName={() => 'compact-row'} />
            <div>
                <AppList/>
            </div>
        </div>
    }
}
