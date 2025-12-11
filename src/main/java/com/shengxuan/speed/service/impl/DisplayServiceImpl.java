package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.Display;
import com.shengxuan.speed.mapper.DisplayMapper;
import com.shengxuan.speed.service.DisplayService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisplayServiceImpl implements DisplayService {
    private final DisplayMapper displayMapper;

    public DisplayServiceImpl(DisplayMapper displayMapper) {
        this.displayMapper = displayMapper;
    }

    @Override
    public List<Display> findByDeviceId(String deviceId,int serverId) {
        return displayMapper.findByDeviceId(deviceId,serverId);
    }
}
