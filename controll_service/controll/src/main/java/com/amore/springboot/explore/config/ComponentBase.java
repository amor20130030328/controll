package com.amore.springboot.explore.config;

import com.amore.springboot.explore.service.app.AppControllService;
import com.amore.springboot.explore.service.app.LocalAppService;
import com.amore.springboot.explore.service.device.DeviceControllService;
import com.amore.springboot.explore.service.device.LocalDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComponentBase {

    @Autowired
    private LocalAppService localAppService;

    @Autowired
    private LocalDeviceService localDeviceService;

    public AppControllService getAppService(){
        return localAppService;

    }

    public DeviceControllService getDeviceService(){
        return localDeviceService;
    }

}
