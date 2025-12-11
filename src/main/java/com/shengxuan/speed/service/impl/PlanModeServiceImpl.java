package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.PlanMode;
import com.shengxuan.speed.mapper.DeviceCtrlModeMapper;
import com.shengxuan.speed.mapper.PlanModeMapper;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.service.PlanModeService;
import com.shengxuan.speed.util.BytesUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanModeServiceImpl implements PlanModeService {

    private final PlanModeMapper planModeMapper;

    public PlanModeServiceImpl(PlanModeMapper planModeMapper) {
        this.planModeMapper = planModeMapper;
    }

    @Override
    public List<PlanMode> findByDeviceId(String deviceId, int serverId) {
        List<PlanMode> planModeList = planModeMapper.findByDeviceId(deviceId,serverId);
        //直接在后端转换为js好解析的格式
        return planModeList;
    }
}
