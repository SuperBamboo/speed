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

    private String ctrlType;
    private String phaseNo;
    private String planNo;
    private String cycleLen;
    private String croodLampNo;
    private String offset;

    private String holdTime;

    public String getCtrlType() {
        return ctrlType;
    }

    public void setCtrlType(String ctrlType) {
        this.ctrlType = ctrlType;
    }

    public String getPhaseNo() {
        return phaseNo;
    }

    public void setPhaseNo(String phaseNo) {
        this.phaseNo = phaseNo;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getCycleLen() {
        return cycleLen;
    }

    public void setCycleLen(String cycleLen) {
        this.cycleLen = cycleLen;
    }

    public String getCroodLampNo() {
        return croodLampNo;
    }

    public void setCroodLampNo(String croodLampNo) {
        this.croodLampNo = croodLampNo;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
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
