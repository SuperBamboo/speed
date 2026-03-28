package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.entity.Plan;
import com.shengxuan.speed.service.PlanService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @RequestMapping("/findByDeviceId")
    public List<Plan> findByDeviceId(String deviceId, int serverId){
        return planService.findByDeviceId(deviceId,serverId);
    }

}
