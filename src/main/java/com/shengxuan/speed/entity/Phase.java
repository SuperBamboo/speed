package com.shengxuan.speed.entity;

import java.io.Serializable;

public class Phase implements Serializable {
    private int serverId;
    private String deviceId;
    private String phaseNo;
    private String phaseName;

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

    public String getPhaseNo() {
        return phaseNo;
    }

    public void setPhaseNo(String phaseNo) {
        this.phaseNo = phaseNo;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
}
