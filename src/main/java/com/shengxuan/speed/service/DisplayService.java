package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Display;

import java.util.List;

public interface DisplayService {

    List<Display> findByDeviceId(String deviceId,int serverId);
}
