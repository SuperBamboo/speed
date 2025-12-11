package com.shengxuan.speed.entity.pojo;


import com.shengxuan.speed.entity.DeviceControlModeApply;
import com.shengxuan.speed.entity.DevicePlanModeApply;
import com.shengxuan.speed.entity.Server;

import java.io.Serializable;

public class Apply implements Serializable {
    private String deviceId;
    private int serverId;
    private String deviceName;
    private String name;
    private int applyType;  //1.手动管控    2.手动程控  3.取消手动管控    4.取消手动程控
    private String applyTypeStr;    //对应applyType
    private String applyTime;

    private String flag = "false";

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private DeviceControlModeApply deviceControlModeApply;
    private DevicePlanModeApply devicePlanModeApply;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public String getApplyTypeStr() {
        return applyTypeStr;
    }

    public void setApplyTypeStr(String applyTypeStr) {
        this.applyTypeStr = applyTypeStr;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public DeviceControlModeApply getDeviceControlModeApply() {
        return deviceControlModeApply;
    }

    public void setDeviceControlModeApply(DeviceControlModeApply deviceControlModeApply) {
        this.deviceControlModeApply = deviceControlModeApply;
    }

    public DevicePlanModeApply getDevicePlanModeApply() {
        return devicePlanModeApply;
    }

    public void setDevicePlanModeApply(DevicePlanModeApply devicePlanModeApply) {
        this.devicePlanModeApply = devicePlanModeApply;
    }
}
