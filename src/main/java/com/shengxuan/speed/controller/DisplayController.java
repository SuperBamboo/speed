package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Display;
import com.shengxuan.speed.service.DisplayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/display")
public class DisplayController {
    private final DisplayService displayService;

    public DisplayController(DisplayService displayService) {
        this.displayService = displayService;
    }

    @RequestMapping("/findByDeviceId")
    public List<Display> findByDeviceId(String deviceId,int serverId){
        return displayService.findByDeviceId(deviceId,serverId);
    }

}
