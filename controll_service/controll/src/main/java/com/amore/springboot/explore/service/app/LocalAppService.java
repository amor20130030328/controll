package com.amore.springboot.explore.service.app;

import com.alibaba.fastjson.JSONObject;
import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.utils.FileUtils;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocalAppService extends AppControllService {

    private List<App> apps ;

    private final Map<String,Integer> appIndexMap = new HashMap<>();

    @PostConstruct
    private void init(){
        if(apps == null || apps.size() == 0){
            String s = FileUtils.readDataFromResource("json/app.json");
            apps = JSONObject.parseArray(s, App.class);
        }

        int index = 0;
        for (App app : this.apps) {
            String key = app.getAppKey();
            appIndexMap.put(key, index);
            index++;
        }
    }


    @Override
    public List<App> getAllApps(){
        try{
            return  apps;
        }catch (Exception e){
            System.out.println("getAllApps");
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, Integer> getAppIndexMap() {
        return appIndexMap;
    }
}
