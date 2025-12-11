package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.mapper.DeviceCtrlModeMapper;
import com.shengxuan.speed.service.DeviceCtrlModeService;
import com.shengxuan.speed.util.BytesUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceCtrlModeServiceImpl implements DeviceCtrlModeService {

    private final DeviceCtrlModeMapper deviceCtrlModeMapper;

    public DeviceCtrlModeServiceImpl(DeviceCtrlModeMapper deviceCtrlModeMapper) {
        this.deviceCtrlModeMapper = deviceCtrlModeMapper;
    }

    @Override
    public List<DeviceCtrlMode> findByDeviceId(String deviceId,int serverId) {
        List<DeviceCtrlMode> deviceCtrlModeList = deviceCtrlModeMapper.findByDeviceId(deviceId,serverId);
        //直接在后端转换为js好解析的格式
        for (DeviceCtrlMode deviceCtrlMode : deviceCtrlModeList) {
            String relayStauDec = deviceCtrlMode.getRelayStau();
            String relayStauBinary = BytesUtil.hexToBinary(BytesUtil.decToHex(relayStauDec));
            deviceCtrlMode.setRelayStau(relayStauBinary);

            String relayStau1Dec = deviceCtrlMode.getRelayStau1();
            String relayStau1Binary = BytesUtil.hexToBinary(BytesUtil.decToHex(relayStau1Dec));
            deviceCtrlMode.setRelayStau1(relayStau1Binary);

        }
        return deviceCtrlModeList;
    }
}
