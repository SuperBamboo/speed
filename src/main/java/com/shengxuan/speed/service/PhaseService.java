package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.entity.Phase;

import java.util.List;

public interface PhaseService {

    List<Phase> findByDeviceId(String deviceId, int serverId);
}
