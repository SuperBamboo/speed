package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.Parameter;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.service.DeviceService;
import com.shengxuan.speed.service.ParameterService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parameter")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @RequestMapping("/findByDeviceId")
    @ResponseBody
    public List<Parameter> findByDeviceId(String deviceId,int serverId) {
        System.out.println("前端发送了查询 "+deviceId+"参数列表");
        List<Parameter> all = parameterService.findByDeviceId(deviceId,serverId);
        return all;
    }
}
