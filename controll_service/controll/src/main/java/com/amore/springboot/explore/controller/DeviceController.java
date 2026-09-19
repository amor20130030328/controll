package com.amore.springboot.explore.controller;


import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.bean.Device;
import com.amore.springboot.explore.service.AppService;
import com.amore.springboot.explore.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AppService appService;

    @GetMapping("get_all_devices")
    public List<Device> getAllDevices(){
        try{
            return  deviceService.getAllDevices();
        }catch (Exception e){
            System.out.println("getAllDevices fail");
        }
        return Arrays.asList();
    }

    @PostMapping("put")
    public void putDevice(@RequestBody Device device){
        try{
            deviceService.insertDevice(device);
        }catch (Exception e){
            System.out.println("putDevice fail");
        }
    }

    @PostMapping("casting")
    public void casting(@RequestBody Device device){
        try{
            deviceService.casting(device);
        }catch (Exception e){
            System.out.println("casting fail");
        }
    }


    @GetMapping("light/{deviceId}/{isUp}")
    public void controllLight(@PathVariable("deviceId") String deviceId,
                        @PathVariable("isUp") boolean isUp){
        try{
            deviceService.controllLight(deviceId,isUp);
        }catch (Exception e){
            System.out.println("casting fail");
        }
    }


    @GetMapping("wireless/{deviceId}")
    public void wireless(@PathVariable("deviceId") String device){
        try{
            deviceService.wireless(device);
        }catch (Exception e){
            System.out.println("casting fail");
        }
    }

    @GetMapping("cmdtype/{cmd}/{deviceId}")
    public void cmdtype(@PathVariable("cmd") String cmd,
                        @PathVariable("deviceId") String device){
        try{
            deviceService.runCmd(device, cmd);
        }catch (Exception e){
            System.out.println("casting fail");
        }
    }

    @GetMapping("restart_adb")
    public void restartAdb(){
        try{
            deviceService.restartAdb();
        }catch (Exception e){
            System.out.println("casting fail");
        }
    }

    //http://localhost/device/always_on_light/192.168.1.3:42889
    @GetMapping("/always_on_light/{deviceId}")
    public void alwaysOnLight(@PathVariable("deviceId") String deviceId){
        try{
            appService.alwaysOnLight(deviceId);
        }catch (Exception e){
            System.out.println("getAllApps");
        }
    }



}
