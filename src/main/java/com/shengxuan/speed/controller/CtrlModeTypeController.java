package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.Display;
import com.shengxuan.speed.service.CtrlModeTypeService;
import com.shengxuan.speed.service.DisplayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ctrlModeType")
public class CtrlModeTypeController {
    private final CtrlModeTypeService ctrlModeTypeService;

    public CtrlModeTypeController(CtrlModeTypeService ctrlModeTypeService) {
        this.ctrlModeTypeService = ctrlModeTypeService;
    }

    @RequestMapping("/findByDeviceId")
    public List<CtrlModeType> findByDeviceId(String deviceId, int serverId){
        return ctrlModeTypeService.findByDeviceId(deviceId,serverId);
    }

}
