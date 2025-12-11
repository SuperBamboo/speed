package com.shengxuan.speed.service;


import com.shengxuan.speed.entity.Port;

import java.util.List;

public interface PortService {

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<Port> findByDeviceId(String deviceId,int serverId);
}
