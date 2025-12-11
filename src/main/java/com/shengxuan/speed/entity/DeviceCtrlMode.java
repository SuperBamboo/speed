package com.shengxuan.speed.entity;

import java.io.Serializable;

public class DeviceCtrlMode implements Serializable {

    private String deviceId;

    private int serverId;
    private String deviceCtrlModeNo;
    private String deviceCtrlModeName;
    private String relayStau;   //输出状态      BIT0-5 分别表示1-6号输出状态   1 开   0 关

    private String relayStauValidity;

    private String relayStau1;   //输出闪烁状态   BIT0-5 分别表示1-6号输出闪烁状态   1 开   0 关 若RelayStau对应位是0 则无效

    private String relayStau1Validity;

    private String displayNo;

    private String displayNoValidity;

    private String warningToneNo;

    private String warningToneNoValidity;

    private String volume;

    private String volumeValidity;

    private String playInteval;

    private String playIntevalValidity;
    private String playNumbers;

    private String playNumbersValidity;

    private String holdTime;

    private String holdTimeValidity;

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

    public String getRelayStauValidity() {
        return relayStauValidity;
    }

    public void setRelayStauValidity(String relayStauValidity) {
        this.relayStauValidity = relayStauValidity;
    }

    public String getRelayStau1Validity() {
        return relayStau1Validity;
    }

    public void setRelayStau1Validity(String relayStau1Validity) {
        this.relayStau1Validity = relayStau1Validity;
    }

    public String getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(String displayNo) {
        this.displayNo = displayNo;
    }

    public String getDisplayNoValidity() {
        return displayNoValidity;
    }

    public void setDisplayNoValidity(String displayNoValidity) {
        this.displayNoValidity = displayNoValidity;
    }

    public String getWarningToneNoValidity() {
        return warningToneNoValidity;
    }

    public void setWarningToneNoValidity(String warningToneNoValidity) {
        this.warningToneNoValidity = warningToneNoValidity;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolumeValidity() {
        return volumeValidity;
    }

    public void setVolumeValidity(String volumeValidity) {
        this.volumeValidity = volumeValidity;
    }

    public String getPlayIntevalValidity() {
        return playIntevalValidity;
    }

    public void setPlayIntevalValidity(String playIntevalValidity) {
        this.playIntevalValidity = playIntevalValidity;
    }

    public String getPlayNumbersValidity() {
        return playNumbersValidity;
    }

    public void setPlayNumbersValidity(String playNumbersValidity) {
        this.playNumbersValidity = playNumbersValidity;
    }

    public String getHoldTimeValidity() {
        return holdTimeValidity;
    }

    public void setHoldTimeValidity(String holdTimeValidity) {
        this.holdTimeValidity = holdTimeValidity;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getPlayInteval() {
        return playInteval;
    }

    public void setPlayInteval(String playInteval) {
        this.playInteval = playInteval;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCtrlModeNo() {
        return deviceCtrlModeNo;
    }

    public void setDeviceCtrlModeNo(String deviceCtrlModeNo) {
        this.deviceCtrlModeNo = deviceCtrlModeNo;
    }

    public String getDeviceCtrlModeName() {
        return deviceCtrlModeName;
    }

    public void setDeviceCtrlModeName(String deviceCtrlModeName) {
        this.deviceCtrlModeName = deviceCtrlModeName;
    }

    public String getRelayStau() {
        return relayStau;
    }

    public void setRelayStau(String relayStau) {
        this.relayStau = relayStau;
    }

    public String getRelayStau1() {
        return relayStau1;
    }

    public void setRelayStau1(String relayStau1) {
        this.relayStau1 = relayStau1;
    }

    public String getWarningToneNo() {
        return warningToneNo;
    }

    public void setWarningToneNo(String warningToneNo) {
        this.warningToneNo = warningToneNo;
    }

    public String getPlayNumbers() {
        return playNumbers;
    }

    public void setPlayNumbers(String playNumbers) {
        this.playNumbers = playNumbers;
    }

    @Override
    public String toString() {
        return "DeviceCtrlMode{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", deviceCtrlModeNo='" + deviceCtrlModeNo + '\'' +
                ", deviceCtrlModeName='" + deviceCtrlModeName + '\'' +
                ", relayStau='" + relayStau + '\'' +
                ", relayStauValidity='" + relayStauValidity + '\'' +
                ", relayStau1='" + relayStau1 + '\'' +
                ", relayStau1Validity='" + relayStau1Validity + '\'' +
                ", displayNo='" + displayNo + '\'' +
                ", displayNoValidity='" + displayNoValidity + '\'' +
                ", warningToneNo='" + warningToneNo + '\'' +
                ", warningToneNoValidity='" + warningToneNoValidity + '\'' +
                ", volume='" + volume + '\'' +
                ", volumeValidity='" + volumeValidity + '\'' +
                ", playInteval='" + playInteval + '\'' +
                ", playIntevalValidity='" + playIntevalValidity + '\'' +
                ", playNumbers='" + playNumbers + '\'' +
                ", playNumbersValidity='" + playNumbersValidity + '\'' +
                ", holdTime='" + holdTime + '\'' +
                ", holdTimeValidity='" + holdTimeValidity + '\'' +
                ", server=" + server +
                '}';
    }
}
