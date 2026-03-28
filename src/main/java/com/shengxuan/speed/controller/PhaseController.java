package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.service.PhaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/phase")
public class PhaseController {
    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @RequestMapping("/findByDeviceId")
    public List<Phase> findByDeviceId(String deviceId, int serverId){
        return phaseService.findByDeviceId(deviceId,serverId);
    }

}
