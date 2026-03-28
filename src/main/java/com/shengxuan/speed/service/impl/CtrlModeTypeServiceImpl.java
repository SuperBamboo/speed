package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.Display;
import com.shengxuan.speed.mapper.CtrlModeTypeMapper;
import com.shengxuan.speed.service.CtrlModeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CtrlModeTypeServiceImpl implements CtrlModeTypeService {

    private final CtrlModeTypeMapper ctrlModeTypeMapper;

    public CtrlModeTypeServiceImpl(CtrlModeTypeMapper ctrlModeTypeMapper) {
        this.ctrlModeTypeMapper = ctrlModeTypeMapper;
    }


    @Override
    public List<CtrlModeType> findByDeviceId(String deviceId, int serverId) {
        return ctrlModeTypeMapper.findByDeviceId(deviceId,serverId);
    }
}
