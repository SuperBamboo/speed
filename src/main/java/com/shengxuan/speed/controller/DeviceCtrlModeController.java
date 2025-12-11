package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.DeviceService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deviceCtrlMode")
public class DeviceCtrlModeController {

    private final DeviceCtrlModeService deviceCtrlModeService;

    public DeviceCtrlModeController(DeviceCtrlModeService deviceCtrlModeService) {
        this.deviceCtrlModeService = deviceCtrlModeService;
    }

    @RequestMapping("/findByDeviceId")
    public List<DeviceCtrlMode> findByDeviceId(String deviceId,int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备DeviceCtrlMode信息");
        return deviceCtrlModeService.findByDeviceId(deviceId,serverId);
    }
}
