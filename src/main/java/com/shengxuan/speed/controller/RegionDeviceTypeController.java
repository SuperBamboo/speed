package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.service.RegionDeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regionDeviceType")
public class RegionDeviceTypeController {

    private final RegionDeviceTypeService regionDeviceTypeService;

    public RegionDeviceTypeController(RegionDeviceTypeService regionDeviceTypeService) {
        this.regionDeviceTypeService = regionDeviceTypeService;
    }

    @RequestMapping("/findByUserId")
    @ResponseBody
    List<RegionDeviceType> findByUserId(Integer uid){
        return regionDeviceTypeService.findByUserId(uid);
    }

    @RequestMapping("/findByCurrentUser")
    @ResponseBody
    List<RegionDeviceType> findByCurrentUser(){
        return regionDeviceTypeService.findByCurrentUser();
    }
}
