package com.shengxuan.speed.entity;

import java.io.Serializable;

public class DeviceControlModeApply implements Serializable {
    private String deviceId;

    private int serverId;
    private String value;
    private String ctrlNo;
    private String relay1;
    private String relay2;

    private String displayNo;
    private String warningTone;

    private String volume;
    private String playInteval;
    private String playNumbers;
    private String holdTime;

    private String applyTime;
    private String flag = "false";    //申请是否被处理

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(String displayNo) {
        this.displayNo = displayNo;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
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

    public String getCtrlNo() {
        return ctrlNo;
    }

    public void setCtrlNo(String ctrlNo) {
        this.ctrlNo = ctrlNo;
    }

    public String getRelay1() {
        return relay1;
    }

    public void setRelay1(String relay1) {
        this.relay1 = relay1;
    }

    public String getRelay2() {
        return relay2;
    }

    public void setRelay2(String relay2) {
        this.relay2 = relay2;
    }

    public String getWarningTone() {
        return warningTone;
    }

    public void setWarningTone(String warningTone) {
        this.warningTone = warningTone;
    }

    public String getPlayInteval() {
        return playInteval;
    }

    public void setPlayInteval(String playInteval) {
        this.playInteval = playInteval;
    }

    public String getPlayNumbers() {
        return playNumbers;
    }

    public void setPlayNumbers(String playNumbers) {
        this.playNumbers = playNumbers;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DeviceControlModeApply{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", value='" + value + '\'' +
                ", ctrlNo='" + ctrlNo + '\'' +
                ", relay1='" + relay1 + '\'' +
                ", relay2='" + relay2 + '\'' +
                ", displayNo='" + displayNo + '\'' +
                ", warningTone='" + warningTone + '\'' +
                ", volume='" + volume + '\'' +
                ", playInteval='" + playInteval + '\'' +
                ", playNumbers='" + playNumbers + '\'' +
                ", holdTime='" + holdTime + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", flag='" + flag + '\'' +
                ", server=" + server +
                '}';
    }
}
