package com.shengxuan.speed.entity;
import java.io.Serializable;

/**
 * 设备端口实体类
 */
public class Port implements Serializable {
    private String deviceId;    //设备id

    private int serverId;
    private String portNo;  //端口id
    private String portName;    //端口名称

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

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        return "Port{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", portNo='" + portNo + '\'' +
                ", portName='" + portName + '\'' +
                '}';
    }
}
