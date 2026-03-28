package com.shengxuan.speed.entity.pojo;

public class NBDeviceParam {
    private String deviceId;
    private int serverId;
    private String parameter;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "NBDeviceParam{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", parameter='" + parameter + '\'' +
                '}';
    }
}
