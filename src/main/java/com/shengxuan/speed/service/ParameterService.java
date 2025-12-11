package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Parameter;

import java.util.List;

public interface ParameterService {
    List<Parameter> findByDeviceId(String deviceId,int serverId);
}
