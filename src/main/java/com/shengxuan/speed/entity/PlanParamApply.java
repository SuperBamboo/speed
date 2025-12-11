package com.shengxuan.speed.entity;
import java.io.Serializable;

public class PlanParamApply implements Serializable {

    private String deviceId;
    private int serverId;
    private String planNo;
    private String planParamNo;
    private String planParamValue;
    private long devicePlanModeApplyId;

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

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getPlanParamNo() {
        return planParamNo;
    }

    public void setPlanParamNo(String planParamNo) {
        this.planParamNo = planParamNo;
    }

    public String getPlanParamValue() {
        return planParamValue;
    }

    public void setPlanParamValue(String planParamValue) {
        this.planParamValue = planParamValue;
    }

    public long getDevicePlanModeApplyId() {
        return devicePlanModeApplyId;
    }

    public void setDevicePlanModeApplyId(long devicePlanModeApplyId) {
        this.devicePlanModeApplyId = devicePlanModeApplyId;
    }
}
