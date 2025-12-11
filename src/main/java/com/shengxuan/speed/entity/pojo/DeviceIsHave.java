package com.shengxuan.speed.entity.pojo;

public class DeviceIsHave {
    private String deviceId;
    private boolean isHave;

    public DeviceIsHave() {
    }

    public DeviceIsHave(String deviceId, boolean isHave) {
        this.deviceId = deviceId;
        this.isHave = isHave;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setHave(boolean have) {
        isHave = have;
    }
}
