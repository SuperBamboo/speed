package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.pojo.PlanParam1;

import java.util.List;

public interface PlanParamService {

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<PlanParam1> findByDeviceIdAndPlanModeNo(String deviceId,String planModeNo,int serverId);
}
