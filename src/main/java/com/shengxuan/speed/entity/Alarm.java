package com.shengxuan.speed.entity;


import java.io.Serializable;

public class Alarm implements Serializable {
    private String deviceId;

    private int serverId;
    private String alarmNo;
    private String alarmName;

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

    public String getAlarmNo() {
        return alarmNo;
    }

    public void setAlarmNo(String alarmNo) {
        this.alarmNo = alarmNo;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", alarmNo='" + alarmNo + '\'' +
                ", alarmName='" + alarmName + '\'' +
                '}';
    }
}
