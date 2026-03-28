package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.mapper.LampGroupMapper;
import com.shengxuan.speed.service.LampGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LampGroupServiceImpl implements LampGroupService {
    private final LampGroupMapper lampGroupMapper;

    public LampGroupServiceImpl(LampGroupMapper lampGroupMapper) {
        this.lampGroupMapper = lampGroupMapper;
    }


    @Override
    public List<LampGroup> findByDeviceId(String deviceId, int serverId) {
        List<LampGroup> lampGroupList = new ArrayList<>();
        LampGroup lampGroup = new LampGroup();
        lampGroup.setServerId(serverId);
        lampGroup.setDeviceId(deviceId);
        lampGroup.setLampGroupNo("0");
        lampGroup.setLampGroupName("无");
        lampGroupList.add(lampGroup);
        List<LampGroup> lampGroupList1 = lampGroupMapper.findByDeviceId(deviceId, serverId);
        lampGroupList.addAll(lampGroupList1);
        return lampGroupList;
    }
}
