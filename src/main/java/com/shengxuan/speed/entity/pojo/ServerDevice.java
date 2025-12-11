package com.shengxuan.speed.entity.pojo;

public class ServerDevice {
    private int serverId;
    private String deviceId;

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

    @Override
    public String toString() {
        return "ServerDevice{" +
                "serverId=" + serverId +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
