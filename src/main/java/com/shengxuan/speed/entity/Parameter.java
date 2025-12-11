package com.shengxuan.speed.entity;

import java.io.Serializable;

/**
 * 特征参数
 */
public class Parameter implements Serializable {
    private String deviceId;    //设备编号

    private int serverId;
    private String parameterNo; //特征序号
    private String parameterName;//特征名称
    private String parameterType;//特征类型
    private String parameterValue;//特征值

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

    public String getParameterNo() {
        return parameterNo;
    }

    public void setParameterNo(String parameterNo) {
        this.parameterNo = parameterNo;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "deviceId='" + deviceId + '\'' +
                ", serverId=" + serverId +
                ", parameterNo='" + parameterNo + '\'' +
                ", parameterName='" + parameterName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", parameterValue='" + parameterValue + '\'' +
                '}';
    }
}
