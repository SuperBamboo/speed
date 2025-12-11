package com.shengxuan.speed.entity.pojo;

/**
 * 判断设备信息是否接受到Bean
 */
public class DeviceReceiver {

    private String deviceID;
    private boolean isReceiver;

    public DeviceReceiver() {
    }

    public DeviceReceiver(String deviceID, boolean isReceiver) {
        this.deviceID = deviceID;
        this.isReceiver = isReceiver;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }
}
