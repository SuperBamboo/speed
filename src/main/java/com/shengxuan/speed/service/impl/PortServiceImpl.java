package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.Port;
import com.shengxuan.speed.entity.WarningTone;
import com.shengxuan.speed.mapper.PortMapper;
import com.shengxuan.speed.mapper.WarningToneMapper;
import com.shengxuan.speed.service.PortService;
import com.shengxuan.speed.service.WarningToneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortServiceImpl implements PortService {

    private final PortMapper portMapper;

    public PortServiceImpl(PortMapper portMapper) {
        this.portMapper = portMapper;
    }

    @Override
    public List<Port> findByDeviceId(String deviceId,int serverId) {
        List<Port> portList = portMapper.findByDeviceId(deviceId,serverId);
        return portList;
    }
}
