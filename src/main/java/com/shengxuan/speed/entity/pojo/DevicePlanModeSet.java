package com.shengxuan.speed.entity.pojo;

import com.shengxuan.speed.entity.PlanParam;

import java.io.Serializable;
import java.util.List;

public class DevicePlanModeSet  implements Serializable {
    private String deviceId;
    private int serverId;
    private String value;
    private String planNo;
    private String save;
    private String auto;
    private String holdTime;
    private String intervalTime;
    private String repetitions;
    private String sysTime;
    private List<PlanParam> planParamList;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<PlanParam> getPlanParamList() {
        return planParamList;
    }

    public void setPlanParamList(List<PlanParam> planParamList) {
        this.planParamList = planParamList;
    }
}
