package com.amore.springboot.explore.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 设备IP信息实体类
 */

@Getter
@Setter
@Data
public class DeviceIpInfo {

    private int id;

    private String deviceId;

    private String publicIp;

    private String lanIp;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
