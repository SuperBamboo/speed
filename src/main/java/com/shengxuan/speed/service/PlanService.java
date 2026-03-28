package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.entity.Plan;

import java.util.List;

public interface PlanService {

    List<Plan> findByDeviceId(String deviceId, int serverId);
}
