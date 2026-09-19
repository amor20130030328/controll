package com.amore.springboot.explore.service.device;

import com.alibaba.fastjson.JSONObject;
import com.amore.springboot.explore.bean.Device;
import com.amore.springboot.explore.utils.ADBUtils;
import com.amore.springboot.explore.utils.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocalDeviceService extends DeviceControllService {

    private Map<String,Device> deviceMap = new HashMap<>();

    @PostConstruct
    private void init(){
        String s = FileUtils.readDataFromResource("json/device.json");
        List<Device> devices = JSONObject.parseArray(s, Device.class);
        devices.forEach(device -> {
            deviceMap.put(device.getDeviceId(), device);
        });
    }

    @Override
    public List<Device> getAllDevices() {
        List<String> deviceIds = ADBUtils.getAllDeviceIds();
        List<Device> deviceList = deviceIds.stream().map(deviceId->{

                Device device = deviceMap.get(deviceId);
                if(device == null){
                    device = new Device();
                    device.setDeviceId(deviceId);
                }
                return device;

        }).collect(Collectors.toList());
        if(deviceList.size() > 0){
            return deviceList;
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public void insertDevice(Device device) {
        deviceMap.put(device.getDeviceId(), device);
    }
}
