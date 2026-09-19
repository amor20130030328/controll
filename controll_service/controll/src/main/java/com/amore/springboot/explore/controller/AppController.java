package com.amore.springboot.explore.controller;

import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.service.AppService;
import com.amore.springboot.explore.service.app.AppControllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("app")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private AppControllService appControllService;

    @GetMapping("get_all_apps")
    public List<App> getAllApps(){
        try{
            return  appService.getAllApps();
        }catch (Exception e){
            System.out.println("getAllApps");
        }
        return Arrays.asList();
    }

    @GetMapping("/casting/{deviceId}")
    public void casting(@PathVariable("deviceId") String deviceId){
        try{
            appService.casting(deviceId);
        }catch (Exception e){
            System.out.println("getAllApps");
        }
    }



    @GetMapping("/open/{app}")
    public void open(@PathVariable("app") String appKey){
        try {
            appService.open(appKey);
        }catch (Exception e){
            System.err.println("open app fail");
        }
    }

    @PostMapping("/move/{isMove}/{up}/{down}")
    public void move(@PathVariable("isMove") boolean isMove,
                          @PathVariable("up") int up,
                          @PathVariable("down") int down,
                         @RequestBody List<String> deviceIds
                     ){
        appService.move(deviceIds);
    }

    @PostMapping("/mutilmove/{isMove}/{up}/{down}")
    public void mutilmove(@PathVariable("isMove") boolean isMove,
                     @PathVariable("up") int up,
                     @PathVariable("down") int down,
                          @RequestBody List<String> deviceIds){
        try {
            appService.mutilmove(deviceIds,isMove,up,down);
        }catch (Exception e){
            System.out.println("move is fail");
        }
    }



    @GetMapping("/installApp/{deviceId}")
    public void installApp(@PathVariable("deviceId") String deviceId){
        try{
            appService.installApp(deviceId);
        }catch (Exception e){
            System.err.println("installApp fail");
        }
    }



    @GetMapping("/move/polling")
    public void Polling(){
        try {
            System.out.println("app move polling start");
            appService.poll();
            System.out.println("app move polling end");
        }catch (Exception e){
            System.err.println("Polling fail");
        }
    }

    @PostMapping("/stop")
    public void stop(){
        try {
            appService.stop();
            appControllService.stop();
        }catch (Exception e){
            System.out.println("move is fail");
        }
    }

}
