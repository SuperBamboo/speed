package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.PushAlarm;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.service.PushAlarmService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pushAlarm")
public class PushAlarmController {

    private final PushAlarmService pushAlarmService;

    public PushAlarmController(PushAlarmService pushAlarmService) {
        this.pushAlarmService = pushAlarmService;
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<PushAlarm> findAll() {
        List<PushAlarm> all = pushAlarmService.findAll();
        return all;
    }

    @RequestMapping("/findAllByDeviceId")
    public List<PushAlarm> findAllByDeviceId(String deviceId,int serverId){
        return pushAlarmService.findAllByDeviceId(deviceId,serverId);
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody PushAlarm searchPushAlarm, int pageNo, int pageSize){
        return pushAlarmService.findPage(searchPushAlarm,pageNo,pageSize);
    }

    @RequestMapping("/updateCheck")
    public void updateCheck(Integer id){
        pushAlarmService.updateCheck(id);
    }

    @RequestMapping("/updateCheckAll")
    public void updateCheckAll(){
        pushAlarmService.updateCheckAll();
    }

    @RequestMapping("/findNewAlarm10Size")
    public List<PushAlarm> findNewAlarm10Size(){
        return pushAlarmService.findNewAlarm10Size();
    }
}
