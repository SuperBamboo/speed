package com.shengxuan.speed.entity;

import java.io.Serializable;

public class LampGroup implements Serializable {
    private int serverId;
    private String deviceId;
    private String lampGroupNo;
    private String lampGroupName;

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

    public String getLampGroupNo() {
        return lampGroupNo;
    }

    public void setLampGroupNo(String lampGroupNo) {
        this.lampGroupNo = lampGroupNo;
    }

    public String getLampGroupName() {
        return lampGroupName;
    }

    public void setLampGroupName(String lampGroupName) {
        this.lampGroupName = lampGroupName;
    }
}
