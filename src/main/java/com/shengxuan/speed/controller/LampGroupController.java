package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.service.LampGroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lampGroup")
public class LampGroupController {
    private final LampGroupService lampGroupService;

    public LampGroupController(LampGroupService lampGroupService) {
        this.lampGroupService = lampGroupService;
    }

    @RequestMapping("/findByDeviceId")
    public List<LampGroup> findByDeviceId(String deviceId, int serverId){
        return lampGroupService.findByDeviceId(deviceId,serverId);
    }

}
