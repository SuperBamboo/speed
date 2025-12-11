package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.WarningTone;
import com.shengxuan.speed.mapper.WarningToneMapper;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.WarningToneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningToneServiceImpl implements WarningToneService {

    private final WarningToneMapper warningToneMapper;

    public WarningToneServiceImpl(WarningToneMapper warningToneMapper) {
        this.warningToneMapper = warningToneMapper;
    }

    @Override
    public List<WarningTone> findByDeviceId(String deviceId,int serverId) {
        List<WarningTone> warningTones = warningToneMapper.findByDeviceId(deviceId,serverId);
        return warningTones;
    }
}
