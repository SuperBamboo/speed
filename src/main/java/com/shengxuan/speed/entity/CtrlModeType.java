package com.shengxuan.speed.entity;

import java.io.Serializable;

public class CtrlModeType implements Serializable {
    private int serverId;
    private String deviceId;
    private String ctrlModeTypeNo;
    private String ctrlModeTypeName;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCtrlModeTypeNo() {
        return ctrlModeTypeNo;
    }

    public void setCtrlModeTypeNo(String ctrlModeTypeNo) {
        this.ctrlModeTypeNo = ctrlModeTypeNo;
    }

    public String getCtrlModeTypeName() {
        return ctrlModeTypeName;
    }

    public void setCtrlModeTypeName(String ctrlModeTypeName) {
        this.ctrlModeTypeName = ctrlModeTypeName;
    }
}
