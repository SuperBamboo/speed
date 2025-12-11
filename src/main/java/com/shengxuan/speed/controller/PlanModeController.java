package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.PlanMode;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.PlanModeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/planMode")
public class PlanModeController {

    private final PlanModeService planModeService;

    public PlanModeController(PlanModeService planModeService) {
        this.planModeService = planModeService;
    }


    @RequestMapping("/findByDeviceId")
    public List<PlanMode> findByDeviceId(String deviceId,int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备PlanMode信息");
        return planModeService.findByDeviceId(deviceId,serverId);
    }
}
