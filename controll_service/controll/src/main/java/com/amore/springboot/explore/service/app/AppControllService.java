package com.amore.springboot.explore.service.app;

import com.alibaba.fastjson.JSONArray;
import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.bean.WindowPointInfo;
import com.amore.springboot.explore.utils.ADBUtils;
import com.amore.springboot.explore.utils.FileUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AppControllService{

    private boolean isMove;

    private ExecutorService executor;

    public AppControllService(){
        this.executor = Executors.newCachedThreadPool();
    }

    public abstract List<App> getAllApps();

    public abstract Map<String,Integer> getAppIndexMap();

    private static Map<String, WindowPointInfo> map = new HashMap<>();


    public void installApp(String deviceId){

        ADBUtils.installApp(deviceId);
    }

    static {
        String s = FileUtils.readDataFromResource("json/point.json");
        List<WindowPointInfo> list = JSONArray.parseArray(s, WindowPointInfo.class);
        list.forEach(item->{
            map.put(item.getDeviceId(), item);
        });
    }

    public void init(String deviceId){
        if (map.containsKey(deviceId)) {
            Gson gson = new Gson();
            String json = gson.toJson(map.get(deviceId));
            ADBUtils.write(deviceId, json);
        }

    }

    public void getWindow(String deviceId){

        ADBUtils.enterTask(deviceId);
    }

    public void casting(String deviceId){

        if("all".equals(deviceId)){
            ADBUtils.casting(ADBUtils.getAllDeviceIds());
        }else{
            ADBUtils.casting(Arrays.asList(deviceId));
        }

    }

    public void alwaysOnLight(String deviceId) {
        if ("all".equals(deviceId)) {
            ADBUtils.alwaysOnLight(ADBUtils.getAllDeviceIds());
        } else {
            ADBUtils.alwaysOnLight(Arrays.asList(deviceId));
        }
    }

    public void move(List<String>  deviceIds){

        this.isMove = true;
        int num = 0 ;
        boolean direction = true;
        while(this.isMove){
            num++;
            if(num % 10 == 0) {
                direction = !direction;
            }
            ADBUtils.move(deviceIds, direction);
            sleep(6);
        }
    }

    public static void sleep(int second){

        try {
            Thread.sleep( second * 1000L);
        } catch (InterruptedException e) {
            System.out.println("睡眠2秒异常");
        }
    }

    public void open(String appKey){
        List<String> deviceIds = ADBUtils.getAllDeviceIds();
        Integer i = getAppIndexMap().get(appKey);
        App app = getAllApps().get(i);
        deviceIds.forEach(deviceId->{
            ADBUtils.CloseApp(deviceId,app.getAppPackageName());
            ADBUtils.OpenApp(deviceId,app.getAppPackageName(),app.getAppActivity());
        });
        System.out.println(app);
    }

    public void mutilmove(List<String> deviceIds,boolean isMove, int up, int down){

        this.isMove = isMove;
        while(this.isMove){
            for(int i=0; i< up && this.isMove ;i++){
                ADBUtils.mutilmove(deviceIds,true);
            }
            for(int i=0; i< down && this.isMove ;i++){
                ADBUtils.mutilmove(deviceIds,true);
            }
        }
    }

    public void stop() {
        this.isMove = false;
    }

}
