package com.amore.springboot.explore.service.device;

import com.amore.springboot.explore.bean.Device;
import com.amore.springboot.explore.mapper.DeviceMapper;
import com.amore.springboot.explore.utils.ADBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OnlineDeviceService extends DeviceControllService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public List<Device> getAllDevices(){

        List<String> deviceIds = ADBUtils.getAllDeviceIds();

        List<Device> devices = deviceIds.stream().map(deviceId -> {
            Device device = null;
            try{
                device = deviceMapper.getDeviceById(deviceId);
            }catch (Exception e){
                System.out.println("联网失败....");
            }

            if (device == null) {
                device = new Device();
                device.setDeviceId(deviceId);
            }
            return device;
        }).collect(Collectors.toList());

        return devices;
    }

    public void insertDevice(Device device){
        if(deviceMapper.getDeviceById(device.getDeviceId()) == null){
            deviceMapper.insert(device);
        }else{
            deviceMapper.update(device);
        }
    }





}
