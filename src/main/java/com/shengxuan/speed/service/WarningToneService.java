package com.shengxuan.speed.service;


import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.WarningTone;

import java.util.List;

public interface WarningToneService {

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<WarningTone> findByDeviceId(String deviceId,int serverId);
}
