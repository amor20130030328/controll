package com.amore.springboot.explore.service.device;


import com.amore.springboot.explore.bean.Device;
import com.amore.springboot.explore.utils.ADBUtils;

import java.util.List;

public abstract class DeviceControllService {

    public abstract List<Device> getAllDevices();

    public abstract void insertDevice(Device device);

    public void controllLight(String deviceId, boolean isUp){
        ADBUtils.controllLight(deviceId,isUp);

    }

    public void casting(Device device){
        String phoneNum = device.getPhoneNum();
        if(phoneNum != null && phoneNum != ""){
            ADBUtils.casting(device.getDeviceId(), device.getPhoneNum());
        }else {
            ADBUtils.casting(device.getDeviceId());
        }
    }

}
