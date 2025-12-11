package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.PlanMode;

import java.util.List;

public interface PlanModeService {

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<PlanMode> findByDeviceId(String deviceId,int serverId);
}
