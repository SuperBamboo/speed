package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.service.DeviceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<Device> findAll() {
        List<Device> all = deviceService.findAll();
        return all;
    }

    @RequestMapping("/findAllAndFlag")
    public List<Device> findAllAndFlag(){
        return deviceService.findAllAndFlag();
    }

    @RequestMapping("/findAllDeviceType")
    public List<String> findAllDeviceType(){
        return deviceService.findAllDeviceType();
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Device searchDevice, int pageNo, int pageSize){


        return deviceService.findPage(searchDevice,pageNo,pageSize);
    }

    @RequestMapping("/findById")
    public Device findById(String deviceId,int serverId){
        System.out.println("前端发送了查询 "+deviceId+" 设备信息");
        return deviceService.findById(deviceId,serverId);
    }
}
