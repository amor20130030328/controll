import React from "react";
import Other from './Other'
import AppList from './AppList'
import DeviceManager from './DeviceManager'
import NginxManager from './NginxManager'
import PortManager from './PortManager'
import NetworkScanner from './NetworkScanner'

export default class AppContent extends React.Component{



    getMenu=(menu)=>{
        let app = <Other/>;
        switch (menu){
            case "install":
                app = <InstallApp/>
                break;
            case "device_manager":
                app = <DeviceManager/>
                break;
            case "start":
                app = <InstallApp/>
                break;
            case "nginx_manager":
                app = <NginxManager/>
                break;
            case "port_manager":
                app = <PortManager/>
                break;
            case "network_scanner":
                app = <NetworkScanner/>
                break;
            default:
                app = <Other/>;
        }
        return app;
    }

    render() {
        let {menu} = this.props;
        return <div>
            {this.getMenu(menu)}
        </div>
    }
}