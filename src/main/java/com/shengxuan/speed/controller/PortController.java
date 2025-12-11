package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Port;
import com.shengxuan.speed.entity.WarningTone;
import com.shengxuan.speed.service.PortService;
import com.shengxuan.speed.service.WarningToneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/port")
public class PortController {

    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }


    @RequestMapping("/findByDeviceId")
    public List<Port> findByDeviceId(String deviceId,int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备PORT信息");
        return portService.findByDeviceId(deviceId,serverId);
    }
}
