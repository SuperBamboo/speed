package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.LampGroup;

import java.util.List;

public interface LampGroupService {

    List<LampGroup> findByDeviceId(String deviceId, int serverId);
}
