package com.shengxuan.speed.entity;


import java.io.Serializable;

public class Display implements Serializable {
    private String deviceId;

    private int serverId;
    private String displayNo;
    private String displayName;
    private String displayColor;

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

    public String getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(String displayNo) {
        this.displayNo = displayNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

    @Override
    public String toString() {
        return "Display{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", displayNo='" + displayNo + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayColor='" + displayColor + '\'' +
                '}';
    }
}
