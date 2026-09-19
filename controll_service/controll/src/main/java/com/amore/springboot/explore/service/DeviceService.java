package com.amore.springboot.explore.service;

import com.alibaba.fastjson.JSONObject;
import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.bean.Device;
import com.amore.springboot.explore.bean.DeviceIpInfo;
import com.amore.springboot.explore.config.ComponentBase;
import com.amore.springboot.explore.service.device.DeviceControllService;
import com.amore.springboot.explore.utils.ADBUtils;
import com.amore.springboot.explore.utils.FileUtils;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeviceService {

    @Autowired
    private ComponentBase componentBase;

    private DeviceControllService deviceControllService;

    private Map<String, DeviceIpInfo> deviceIpMap = new ConcurrentHashMap<String, DeviceIpInfo>();


    private void initState(){
        deviceControllService = componentBase.getDeviceService();

        Map<String, String> deviceInfoMap = new HashMap<String, String>();
        String s = FileUtils.readDataFromResource("json/deviceinfo.json");
        List<DeviceInfo> deviceInfos = JSONObject.parseArray(s, DeviceInfo.class);
        for (DeviceInfo deviceInfo : deviceInfos) {
            deviceInfoMap.put(deviceInfo.getAndroidId(), deviceInfo.getDeviceId());
        }

        System.out.println("初始化完成！！" + deviceIpMap);
    }

    @PostConstruct
    private void init(){
        initState();
    }

    @Scheduled(fixedRate = 10000)
    private void refresh(){
        //initState();
    }

    public List<Device> getAllDevices(){
        List<Device> devices = deviceControllService.getAllDevices();
        return devices;
    }

    public void insertDevice(Device device){
        deviceControllService.insertDevice(device);
    }

    public void casting(Device device){
        deviceControllService.casting(device);
    }

    public void controllLight(String deviceId, boolean isUp){
        deviceControllService.controllLight(deviceId, isUp);
    }

    public boolean wireless(String deviceId) {
        String ip = ADBUtils.getLocalAreaIp(deviceId);
        ADBUtils.wireless(deviceId, ip);
        return true;
    }



    public void runCmd(String deviceId, String cmdType) {

        ADBUtils.runCmd(deviceId, cmdType);
    }

    public void restartAdb() {

        ADBUtils.restart();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeviceInfo {
        private String deviceId;
        private String androidId;
    }

}
