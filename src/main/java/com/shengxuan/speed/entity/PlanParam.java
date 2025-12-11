package com.shengxuan.speed.entity;

import java.io.Serializable;

public class PlanParam implements Serializable {
    private String deviceId;
    private int serverId;
    private String planModeNo;
    private String planParamNo;
    private String planParamName;
    private String defaultValue;

    private boolean isHavePlanParamValue;

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

    public String getPlanParamName() {
        return planParamName;
    }

    public void setPlanParamName(String planParamName) {
        this.planParamName = planParamName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isHavePlanParamValue() {
        return isHavePlanParamValue;
    }

    public void setHavePlanParamValue(boolean havePlanParamValue) {
        isHavePlanParamValue = havePlanParamValue;
    }

    public String getPlanModeNo() {
        return planModeNo;
    }

    public void setPlanModeNo(String planModeNo) {
        this.planModeNo = planModeNo;
    }

    @Override
    public String toString() {
        return "PlanParam{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", planModeNo='" + planModeNo + '\'' +
                ", planParamNo='" + planParamNo + '\'' +
                ", planParamName='" + planParamName + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", isHavePlanParamValue=" + isHavePlanParamValue +
                '}';
    }
}
