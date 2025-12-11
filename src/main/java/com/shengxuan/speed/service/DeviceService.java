package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.pojo.*;

import java.util.List;

public interface DeviceService {
    List<Device> findAll();

    /**
     * 分页条件查询
     * @param device
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(Device device, int pageNo, int pageSize);

    Device findById(String deviceId,int serverId);

    List<RegionAndSubRegion> findAllRegion();

    List<String> findAllDeviceType();
    List<Device> findAllAndFlag();

//    /**
//     * 查询所有区域包含子区及其设备
//     * @return
//     */
//    List<RegionAndSubRegionAndDevice> findAllRegionAndSubRegionAndDevice();

    /**
     * 查询所有server区域包含子区及其设备
     * @return
     */
    List<ServerAndRegionAndSubRegionAndDevice> findAllServerAndRegionAndSubRegionAndDevice();

}
