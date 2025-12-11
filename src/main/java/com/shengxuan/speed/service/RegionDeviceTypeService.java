package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.RegionDeviceType;

import java.util.List;

public interface RegionDeviceTypeService {
    List<RegionDeviceType> findByUserId(Integer uid);

    List<RegionDeviceType> findByCurrentUser();
}
