package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.pojo.PlanParam1;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.PlanParamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/planParam")
public class PlanParamController {

    private final PlanParamService planParamService;

    public PlanParamController(PlanParamService planParamService) {
        this.planParamService = planParamService;
    }


    @RequestMapping("/findByDeviceIdAndPlanModeNo")
    public List<PlanParam1> findByDeviceId(@RequestParam String deviceId, @RequestParam String planModeNo,@RequestParam int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备planParam1信息");
        return planParamService.findByDeviceIdAndPlanModeNo(deviceId,planModeNo,serverId);
    }
}
