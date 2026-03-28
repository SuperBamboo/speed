package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.Display;

import java.util.List;

public interface CtrlModeTypeService {

    List<CtrlModeType> findByDeviceId(String deviceId, int serverId);
}
