package com.shengxuan.speed.entity;


import java.io.Serializable;

public class PlanMode implements Serializable {
    private String deviceId;
    private int serverId;
    private String planModeNo;
    private String planModeName;

    private String holdTime;

    private String repetitions;

    private String intervalTime;

    private String sysTime;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlanModeNo() {
        return planModeNo;
    }

    public void setPlanModeNo(String planModeNo) {
        this.planModeNo = planModeNo;
    }

    public String getPlanModeName() {
        return planModeName;
    }

    public void setPlanModeName(String planModeName) {
        this.planModeName = planModeName;
    }

    @Override
    public String toString() {
        return "PlanMode{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", planModeNo='" + planModeNo + '\'' +
                ", planModeName='" + planModeName + '\'' +
                ", holdTime='" + holdTime + '\'' +
                ", repetitions='" + repetitions + '\'' +
                ", intervalTime='" + intervalTime + '\'' +
                ", sysTime='" + sysTime + '\'' +
                '}';
    }
}
