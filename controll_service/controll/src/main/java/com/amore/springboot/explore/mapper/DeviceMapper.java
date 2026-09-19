package com.amore.springboot.explore.mapper;

import com.amore.springboot.explore.bean.App;
import com.amore.springboot.explore.bean.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    public List<Device> getAllDevice();

    public Device getDeviceById(@Param("deviceId") String deviceId);

    public boolean insert(Device device);

    public boolean update(Device device);

    public boolean batchInsert(List<Device> devices);

}
