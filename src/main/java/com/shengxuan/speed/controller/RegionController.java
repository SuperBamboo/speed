package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.pojo.RegionAndSubRegion;
import com.shengxuan.speed.entity.pojo.ServerAndRegionAndSubRegionAndDevice;
import com.shengxuan.speed.service.DeviceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {

    private final DeviceService deviceService;

    public RegionController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping("/findAllRegion")
    public List<RegionAndSubRegion> findAllRegion(){
        List<RegionAndSubRegion> list = new ArrayList<>();
        list = deviceService.findAllRegion();
        return list;
    }

    /*@RequestMapping("/findAllRegionAndSubRegionAndDevice")
    public List<RegionAndSubRegionAndDevice> findAllRegionAndSubRegionAndDevice(){
        return deviceService.findAllRegionAndSubRegionAndDevice();
    }*/

    @RequestMapping("/findAllServerAndRegionAndSubRegionAndDevice")
    public List<ServerAndRegionAndSubRegionAndDevice> findAllRegionAndSubRegionAndDevice(){
        return deviceService.findAllServerAndRegionAndSubRegionAndDevice();
    }

}
