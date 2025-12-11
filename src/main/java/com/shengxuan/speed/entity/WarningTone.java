package com.shengxuan.speed.entity;


import java.io.Serializable;

public class WarningTone implements Serializable {
    private String deviceId;

    private int serverId;
    private String warningToneNo;
    private String warningToneName;
    private String warningToneLen;

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

    public String getWarningToneNo() {
        return warningToneNo;
    }

    public void setWarningToneNo(String warningToneNo) {
        this.warningToneNo = warningToneNo;
    }

    public String getWarningToneName() {
        return warningToneName;
    }

    public void setWarningToneName(String warningToneName) {
        this.warningToneName = warningToneName;
    }

    public String getWarningToneLen() {
        return warningToneLen;
    }

    public void setWarningToneLen(String warningToneLen) {
        this.warningToneLen = warningToneLen;
    }

    @Override
    public String toString() {
        return "WarningTone{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", warningToneNo='" + warningToneNo + '\'' +
                ", warningToneName='" + warningToneName + '\'' +
                ", warningToneLen='" + warningToneLen + '\'' +
                '}';
    }
}
