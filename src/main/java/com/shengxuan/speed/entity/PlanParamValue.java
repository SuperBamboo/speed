package com.shengxuan.speed.entity;

import java.io.Serializable;

public class PlanParamValue implements Serializable {
    private String deviceId;
    private int serverId;
    private String planModeNo;
    private String planParamNo;
    private String planParamValueNo;
    private String planParamValueName;

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

    public String getPlanParamNo() {
        return planParamNo;
    }

    public void setPlanParamNo(String planParamNo) {
        this.planParamNo = planParamNo;
    }

    public String getPlanParamValueNo() {
        return planParamValueNo;
    }

    public void setPlanParamValueNo(String planParamValueNo) {
        this.planParamValueNo = planParamValueNo;
    }

    public String getPlanParamValueName() {
        return planParamValueName;
    }

    public void setPlanParamValueName(String planParamValueName) {
        this.planParamValueName = planParamValueName;
    }

    public String getPlanModeNo() {
        return planModeNo;
    }

    public void setPlanModeNo(String planModeNo) {
        this.planModeNo = planModeNo;
    }

    @Override
    public String toString() {
        return "PlanParamValue{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", planModeNo='" + planModeNo + '\'' +
                ", planParamNo='" + planParamNo + '\'' +
                ", planParamValueNo='" + planParamValueNo + '\'' +
                ", planParamValueName='" + planParamValueName + '\'' +
                '}';
    }
}
