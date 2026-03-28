package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.entity.Plan;
import com.shengxuan.speed.mapper.PlanMapper;
import com.shengxuan.speed.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanMapper planMapper;

    public PlanServiceImpl(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    @Override
    public List<Plan> findByDeviceId(String deviceId, int serverId) {
        List<Plan> planList = new ArrayList<>();
        Plan plan = new Plan();
        plan.setServerId(serverId);
        plan.setDeviceId(deviceId);
        plan.setPlanNo("0");
        plan.setPlanName("空");
        planList.add(plan);
        List<Plan> planList1 = planMapper.findByDeviceId(deviceId, serverId);
        planList.addAll(planList1);
        return planList;
    }
}
