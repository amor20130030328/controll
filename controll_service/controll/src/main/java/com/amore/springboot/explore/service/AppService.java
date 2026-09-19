package com.amore.springboot.explore.service;

import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.config.ComponentBase;
import com.amore.springboot.explore.service.app.AppControllService;
import com.amore.springboot.explore.utils.ADBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.amore.springboot.explore.utils.ADBUtils.takeOn;
import static com.amore.springboot.explore.utils.FileUtils.writeStringToFile;

@Service
public class AppService {

    private static int MOVE_LOOP_MAX_TIMES = 100;
    private static int SLEEP_SECONDS = 3;
    private static int OPEN_SLEEP_SECONDS = 5;

    private AppControllService appControllService;

    @Autowired
    private ComponentBase componentBase;

    private boolean isMove;

    private String singleDevice;

    @PostConstruct
    private void init(){
        appControllService = componentBase.getAppService();
    }


    public void stop(){
        this.isMove = false;
    }

    public void doMove(List<String> devices) {
        appControllService.move(devices);
    }

    public void move(List<String>  deviceIds){

        new Thread(() -> {
            doMove(deviceIds);
        }).start();
    }

    public void mutilmove(List<String> deviceIds,boolean isMove, int up, int down){
       appControllService.mutilmove(deviceIds, isMove, up, down);
    }

    public void installApp(String deviceId){
        appControllService.installApp(deviceId);
    }

    public void init(String deviceId){
        appControllService.init(deviceId);
    }

    public void getWindow(String deviceId) {
        appControllService.getWindow(deviceId);
    }


    public List<App> getAllApps(){
       return appControllService.getAllApps();
    }

    public void open(String appKey){
        appControllService.open(appKey);
        sleep(2);
    }

    public void setSingleDevice(String deviceId){
        singleDevice = deviceId;
    }

    public void sync_poll() {
        this.isMove = true;
        int num = 0 ;
        while (this.isMove) {
            takeOn();
            List<App> apps = getAllApps();
            for (App app : apps) {

                if (!app.isMove()) {
                    continue;
                }

                System.out.println("打开 " + app.getAppName() + " num=" + num);
                List<String>  deviceIds = ADBUtils.getAllDeviceIds().stream().
                        filter(deviceId->!deviceId.equals(singleDevice))
                        .collect(Collectors.toList());

                deviceIds.forEach(deviceId->{
                    ADBUtils.CloseApp(deviceId,app.getAppPackageName());
                    ADBUtils.OpenApp(deviceId,app.getAppPackageName(),app.getAppActivity());
                });

                sleep(OPEN_SLEEP_SECONDS);
                int moveCount = 0;
                boolean direction = true;
                while (this.isMove && moveCount < app.getMoveCount()) {
                    ADBUtils.move(deviceIds, direction);
                    moveCount++;
                    System.out.println(app.getAppName() + "  " + moveCount);
                    // 最后一次循环无需休眠，减少无效等待
                    if(moveCount % 10 == 0 ) {
                        direction = !direction;
                    }
                    if (moveCount < MOVE_LOOP_MAX_TIMES) {
                        System.out.println(String.format("app.getMoveCount() %s ,sleep %s",app.getMoveCount(), app.getSleep()));
                        sleep(app.getSleep());

                    }

                    if (!this.isMove) {
                        return;
                    }
                }
            }
        }
    }


    public void poll() {
        new Thread(this::sync_poll).start();
    }


    public void doAllApp(){

    }

    public void casting(String deviceId){
        appControllService.casting(deviceId);
    }

    public void alwaysOnLight(String deviceId){
        appControllService.alwaysOnLight(deviceId);
    }


    public void sleep(int second){

        try {
            Thread.sleep( second * 1000L);
        } catch (InterruptedException e) {
            System.out.println("睡眠2秒异常");
        }
    }
}
