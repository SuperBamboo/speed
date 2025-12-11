package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

public class DeviceControlModeSet implements Serializable {
    private String deviceId;
    private int serverId;
    private String value;
    private String ctrlNo;
    private String save;
    private String auto;
    private String relay1;
    private String relay2;
    private String displayNo;
    private String warningTone;
    private String volume;
    private String playInteval;
    private String playNumbers;
    private String holdTime;

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
}
