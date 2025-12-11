package com.shengxuan.speed.service;


import com.shengxuan.speed.entity.DeviceCtrlMode;

import java.util.List;

public interface DeviceCtrlModeService {

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<DeviceCtrlMode> findByDeviceId(String deviceId,int serverId);
}
