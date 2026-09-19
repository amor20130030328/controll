package com.amore.springboot.explore.mapper;

import com.amore.springboot.explore.bean.DeviceIpInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceIpInfoMapper {

    public List<DeviceIpInfo> getAllDevice();

    public DeviceIpInfo getDeviceById(@Param("deviceId") String deviceId);

    public boolean delete(@Param("deviceId") String deviceId);

    public boolean insert(DeviceIpInfo device);

    public boolean update(DeviceIpInfo device);

    public boolean batchInsert(List<DeviceIpInfo> devices);
}
