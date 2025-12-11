package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.Parameter;
import com.shengxuan.speed.mapper.ParameterMapper;
import com.shengxuan.speed.service.ParameterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterServiceImpl implements ParameterService {

    private final ParameterMapper parameterMapper;

    public ParameterServiceImpl(ParameterMapper parameterMapper) {
        this.parameterMapper = parameterMapper;
    }


    @Override
    public List<Parameter> findByDeviceId(String deviceId,int serverId) {
        return parameterMapper.findByDeviceId(deviceId,serverId);
    }
}
