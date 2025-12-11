package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;
import java.util.List;

public class DeviceStau implements Serializable {
    private String deviceId;

    private int serverId;
    private String deviceName;
    private String detectTime;
    private String ctrlMode;
    private String tempCtrl;
    private String tempPlan;
    private List<DetectStau> detectStauList;

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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(String detectTime) {
        this.detectTime = detectTime;
    }

    public String getCtrlMode() {
        return ctrlMode;
    }

    public void setCtrlMode(String ctrlMode) {
        this.ctrlMode = ctrlMode;
    }

    public String getTempCtrl() {
        return tempCtrl;
    }

    public void setTempCtrl(String tempCtrl) {
        this.tempCtrl = tempCtrl;
    }

    public String getTempPlan() {
        return tempPlan;
    }

    public void setTempPlan(String tempPlan) {
        this.tempPlan = tempPlan;
    }

    public List<DetectStau> getDetectStauList() {
        return detectStauList;
    }

    public void setDetectStauList(List<DetectStau> detectStauList) {
        this.detectStauList = detectStauList;
    }
}
