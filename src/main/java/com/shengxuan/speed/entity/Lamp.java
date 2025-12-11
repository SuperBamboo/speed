package com.shengxuan.speed.entity;

import java.io.Serializable;

/**
 * 信号灯
 */
public class Lamp implements Serializable {

    private String deviceId;    //路口编号 对应设备id
    private String lampId;      //信号灯编号
    private String lampName;    //灯组名称
    private String lampType;    //信号灯类型
    private String lampSize;    //信号灯规格
    private String installDirection;    //安装方位
    private String indicateDirection;   //指示方位
    private String lampUnit1;    //单元组1
    private String lampUnit2;   //单元组2
    private String lampUnit3;   //单元组3
    private String supplier;    //供应商
    private String serviceDate; //投用日期

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLampId() {
        return lampId;
    }

    public void setLampId(String lampId) {
        this.lampId = lampId;
    }

    public String getLampName() {
        return lampName;
    }

    public void setLampName(String lampName) {
        this.lampName = lampName;
    }

    public String getLampType() {
        return lampType;
    }

    public void setLampType(String lampType) {
        this.lampType = lampType;
    }

    public String getLampSize() {
        return lampSize;
    }

    public void setLampSize(String lampSize) {
        this.lampSize = lampSize;
    }

    public String getInstallDirection() {
        return installDirection;
    }

    public void setInstallDirection(String installDirection) {
        this.installDirection = installDirection;
    }

    public String getIndicateDirection() {
        return indicateDirection;
    }

    public void setIndicateDirection(String indicateDirection) {
        this.indicateDirection = indicateDirection;
    }

    public String getLampUnit1() {
        return lampUnit1;
    }

    public void setLampUnit1(String lampUnit1) {
        this.lampUnit1 = lampUnit1;
    }

    public String getLampUnit2() {
        return lampUnit2;
    }

    public void setLampUnit2(String lampUnit2) {
        this.lampUnit2 = lampUnit2;
    }

    public String getLampUnit3() {
        return lampUnit3;
    }

    public void setLampUnit3(String lampUnit3) {
        this.lampUnit3 = lampUnit3;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    @Override
    public String toString() {
        return "Lamp{" +
                "deviceId='" + deviceId + '\'' +
                ", lampId='" + lampId + '\'' +
                ", lampName='" + lampName + '\'' +
                ", lampType='" + lampType + '\'' +
                ", lampSize='" + lampSize + '\'' +
                ", installDirection='" + installDirection + '\'' +
                ", indicateDirection='" + indicateDirection + '\'' +
                ", lampUnit1='" + lampUnit1 + '\'' +
                ", lampUnit2='" + lampUnit2 + '\'' +
                ", lampUnit3='" + lampUnit3 + '\'' +
                ", supplier='" + supplier + '\'' +
                ", serviceDate='" + serviceDate + '\'' +
                '}';
    }
}
