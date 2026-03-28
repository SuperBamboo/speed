package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.mapper.PhaseMapper;
import com.shengxuan.speed.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseMapper phaseMapper;

    public PhaseServiceImpl(PhaseMapper phaseMapper) {
        this.phaseMapper = phaseMapper;
    }

    @Override
    public List<Phase> findByDeviceId(String deviceId, int serverId) {
        List<Phase> phaseList = new ArrayList<>();
        Phase phase = new Phase();
        phase.setServerId(serverId);
        phase.setDeviceId(deviceId);
        phase.setPhaseNo("0");
        phase.setPhaseName("空");
        phaseList.add(phase);
        List<Phase> phaseList1 = phaseMapper.findByDeviceId(deviceId, serverId);
        phaseList.addAll(phaseList1);
        return phaseList;
    }
}
