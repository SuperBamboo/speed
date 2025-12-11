package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.WarningTone;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.WarningToneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warningTone")
public class WarningToneController {

    private final WarningToneService warningToneService;

    public WarningToneController(WarningToneService warningToneService) {
        this.warningToneService = warningToneService;
    }


    @RequestMapping("/findByDeviceId")
    public List<WarningTone> findByDeviceId(String deviceId,int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备WarningTone信息");
        List<WarningTone> warningToneList = warningToneService.findByDeviceId(deviceId,serverId);
        WarningTone warningTone = new WarningTone();
        warningTone.setWarningToneNo("0");
        warningTone.setWarningToneName("无");
        warningToneList.add(0,warningTone);
        return warningToneList;
    }
}
